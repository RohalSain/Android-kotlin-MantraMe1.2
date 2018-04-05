package com.developer.rohal.mantra


import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.support.v7.widget.GridLayoutManager
import android.util.Log
import kotlinx.android.synthetic.main.fragment_fragment_dashboard.*
import kotlinx.android.synthetic.main.fragment_fragment_dashboard.view.*
import android.widget.Toast
import com.github.florent37.singledateandtimepicker.SingleDateAndTimePicker
import java.util.*
import android.support.v4.app.Fragment
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.AppCompatActivity
import android.view.*
import kotlinx.android.synthetic.main.show_box.view.*
import kotlinx.android.synthetic.main.dialog_box.view.*
import kotlinx.android.synthetic.main.fragment_alarm.view.*
import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.ResponseBody
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList
class fragmentDashboard : Fragment() {
    var contextD:Context?=null
    var monthName = arrayOf("Jan", "Feb", "March", "April", "May", "June", "July", "Aug", "Sept", "Oct", "Nov", "Dec")
    var getview: View? = null
    var bitmap: Bitmap? = null
    var bitmap_uri: Uri? = null
    var pos: Int? = null
    var mview: View? = null
    //
    private val SPLASH_TIME_OUT = 3000
    var f: File? = null
    private val PICK_FROM_FILE = 300
    var bmpuri: Bitmap? = null
    private var mCapturedImageURI: Uri? = null
    val REQUEST_IMAGE_CAPTURE = 1
    private var url_pic: Uri? = null
    var bmpString: String = " "
    var bmpByteArray: ByteArray? = null
    var count = 0;
    //
    var loader = Loader()
    var listOfQuote: ArrayList<PojoAllQuoteDetailDashBoard> = ArrayList<PojoAllQuoteDetailDashBoard>();
    override fun onActivityCreated(savedInstanceState: Bundle?) {

        super.onActivityCreated(savedInstanceState)
        var session = Session(context?.applicationContext!!)
        userPic.setImageURI(Uri.parse("${session.getPrfofilePic()}"))
        displayName.text=session.getName()
        menu.setOnClickListener {
            mDrawer.openDrawer(Gravity.START)
        }
        mDrawer.setOnClickListener {
            Log.d("yes", "yes")
        }
        val mDrawerLayout = view!!.findViewById(R.id.mDrawer) as DrawerLayout

        view!!.isFocusableInTouchMode = true
        view!!.requestFocus()
        view!!.setOnKeyListener(object : View.OnKeyListener {
            override fun onKey(v: View, keyCode: Int, event: KeyEvent): Boolean {
                if (event.getAction() === KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
                            //drawer is open
                            mDrawerLayout.closeDrawers();
                            return false
                        } else {

                            onBack()
                            return true
                        }

                    }

                }
                return false
            }
        })


    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mview = inflater!!.inflate(R.layout.fragment_fragment_dashboard, container, false)
        return mview
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var third = LoginDataUser.instance
        contextD = getContext()
        var count = third.count
        mview!!.isFocusableInTouchMode = true
        mview!!.requestFocus()
        mview!!.setOnKeyListener(object : View.OnKeyListener {
            override fun onKey(v: View, keyCode: Int, event: KeyEvent): Boolean {
                if (event.getAction() === KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        if (mDrawer.isDrawerOpen(GravityCompat.START)) {
                            //drawer is open
                            mDrawer.closeDrawers();
                            return false
                        } else {

                            onBack()

                            return true
                        }

                    }

                }
                return false
            }
        })


        if (count == 0) {
            if(listOfQuote.size!=0)
            {
                listOfQuote.clear()
            }
            loader.ShowCustomLoader(contextD!!)
            Log.d("profilepic", "${third.profiePic}")
            userPic.setImageURI(Uri.parse("${third.profiePic}"))
            Log.d("Show", "yes")
            Log.d("Data", "${third.token}")
            val okHttpClient = OkHttpClient.Builder()
                    .addInterceptor(BasicAuthInterceptor("mantrame@emilence.com", "Emilence@1"))
                    .connectTimeout(20, TimeUnit.SECONDS)
                    .writeTimeout(20, TimeUnit.SECONDS)
                    .readTimeout(20, TimeUnit.SECONDS)
                    .build()
            val retrofit = Retrofit.Builder()
                    .client(okHttpClient)
                    .baseUrl("http://139.59.18.239:6010/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            val redditAPI = retrofit.create(RedditAPI::class.java)
            val headerMap = HashMap<String, String>()
            var obj = LoginDataUser.instance
            val redditAP = retrofit.create(RedditAPI::class.java)
            var call = redditAP.GetQuote("${obj.token}")
            call.enqueue(object : Callback<ResponseBody> {
                override fun onResponse(call: Call<ResponseBody>, response: retrofit2.Response<ResponseBody>?) {
                    Log.d("server", "onResponse: Server Response: " + response.toString());

                    try {
                        if (listOfQuote.size != 0) {
                            listOfQuote.clear()
                        }
                        var json: String? = null
                        json = response?.body()?.string()
                        if (json == null) {
                            Log.d("Get Quote", "yes")
                            loader.HideCustomLoader()
                        } else {
                            Log.d("Api", "yes")
                            Log.d("JSON Get Quote", "onResponse: json: " + json);
                            var data: JSONObject? = null;
                            data = JSONObject(json);
                            var cast: JSONArray = data.getJSONArray("data");
                            Log.d("Array of Quotes are", "${cast}")
                            loader.HideCustomLoader()
                            var statusDate: String? = null
                            var statusTime: String? = null
                            var year: StringBuffer = StringBuffer("")
                            var month: StringBuffer = StringBuffer("")
                            var date: StringBuffer = StringBuffer("")
                            var hours: StringBuffer = StringBuffer("")
                            var min: StringBuffer = StringBuffer("")
                            for (i in 0 until cast.length()) {
                                statusDate = null
                                statusTime = null
                                year = StringBuffer()
                                month = StringBuffer()
                                date = StringBuffer()
                                hours = StringBuffer()
                                min = StringBuffer()
                                val actor = cast.getJSONObject(i)
                                var id = actor.getString("_id")
                                var text = actor.getString("text")
                                var background = actor.getString("background")
                                var status = actor.getString("favorite")
                                var Date = actor.getString("createdAt")
                                for (j in 0..3)
                                    year.append(Date[j])
                                for (j in 5..6)
                                    month.append(Date[j])
                                for (j in 8..9)
                                    date.append(Date[j])
                                for (j in 11..12)
                                    hours.append(Date[j])
                                for (j in 14..15)
                                    min.append(Date[j])
                                Log.d("Date is ", "$date $month $year  $hours: $min")
                                var temp = month.toString().toInt()
                                Log.d("month is", "${temp}")
                                statusDate = "$date ${monthName[--temp]} $year"
                                Log.d("Date", "Status Date is $statusDate")
                                var tempHours = hours.toString().toInt()
                                var tempMinutes = min.toString().toInt()
                                if (tempHours == 12 && tempMinutes == 0) {
                                    Log.d("Time", "Time is ${tempHours}:${tempMinutes} noon")
                                    statusTime = "${tempHours}:${tempMinutes} noon"
                                } else if (tempHours > 12) {
                                    tempHours = tempHours - 12
                                    Log.d("Time", "Time is ${tempHours}:${tempMinutes} PM")
                                    statusTime = "${tempHours}:${tempMinutes} PM"
                                } else {
                                    Log.d("Time", "Time is ${tempHours}:${tempMinutes} AM")
                                    statusTime = "${tempHours}:${tempMinutes} AM"
                                }

                                listOfQuote.add(PojoAllQuoteDetailDashBoard(id, text, background, status, statusDate, statusTime))

                            }
                            for (i in 0 until listOfQuote.size) {
                                var item: PojoAllQuoteDetailDashBoard = listOfQuote.get(i)
                                Log.d("Entry ${i}", "${item.id}  ${item.text}  ${item.background}")

                            }
                            var session = Session(context?.applicationContext!!)
                            var adapter = CustomAdpater(listOfQuote, contextD!!, object : CustomAdpater.OnViewClicked {
                                override fun onClick(position: Int) {
                                    Log.d("Clicked", "yes ${position}")
                                    var obi = LoginDataUser.instance
                                    var item: PojoAllQuoteDetailDashBoard = listOfQuote.get(position)
                                    obi.QuoteID = item.id
                                    pos = position
                                    //Photo_box(context)
                                }
                            })
                            var manager = GridLayoutManager(context, 1)
                            recyclerview?.layoutManager = manager
                            recyclerview?.adapter = adapter
                        }


                    } catch (e: JSONException) {
                        var use = ReuseMethod()
                        //use.showSnakBar(getview,"JSONException")
                        Log.e("JSONException", "onResponse: JSON Exception: ");
                    } catch (e: IOException) {
                        var use = ReuseMethod()
                        // use.showSnakBar(getview,"JSON Exception")
                        Log.e("IOexception", "onResponse: JSONException: ");
                    }

                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Log.e("OnFailure", "onFailure: Something went wrong: ");
                    var use = ReuseMethod()
                    //use.showSnakBar(getview,"Something went wrong")
                    //Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            })
            LoginDataUser.instance.count++

        } else {
            Log.d("String", "${listOfQuote.size}")
                var client = ApiCall()
                var retrofit = client.retrofitClient()
                val redditAPI = retrofit?.create(RedditAPI::class.java)
                val headerMap = HashMap<String, String>()
                var obj = LoginDataUser.instance
                val redditAP = retrofit?.create(RedditAPI::class.java)
                var call = redditAP?.GetQuote("${obj.token}")
                call?.enqueue(object : Callback<ResponseBody> {
                    override fun onResponse(call: Call<ResponseBody>, response: retrofit2.Response<ResponseBody>?) {
                        Log.d("server", "onResponse: Server Response: " + response.toString());

                        try {
                            if (listOfQuote.size != 0) {
                                listOfQuote.clear()
                            }
                            var json: String? = null
                            json = response?.body()?.string()
                            if (json == null) {
                                Log.d("Get Quote", "yes")
                                loader.HideCustomLoader()
                            } else {
                                Log.d("Api", "yes")
                                Log.d("JSON Get Quote", "onResponse: json: " + json);
                                var data1: JSONObject? = null;
                                data1 = JSONObject(json);
                                var cast: JSONArray = data1.getJSONArray("data");
                                Log.d("Array of Quotes are", "${cast}")
                                loader.HideCustomLoader()
                                var statusDate1: String? = null
                                var statusTime1: String? = null
                                var year1: StringBuffer = StringBuffer()
                                var month1: StringBuffer = StringBuffer()
                                var date1: StringBuffer = StringBuffer()
                                var hours1: StringBuffer = StringBuffer()
                                var min1: StringBuffer = StringBuffer()
                                for (i in 0 until cast.length()) {
                                    statusDate1 = null
                                    statusTime1 = null
                                    year1 = StringBuffer()
                                    month1 = StringBuffer()
                                    date1 = StringBuffer()
                                    hours1 = StringBuffer()
                                    min1 = StringBuffer()
                                    val actor = cast.getJSONObject(i)
                                    var id = actor.getString("_id")
                                    var text = actor.getString("text")
                                    var background = actor.getString("background")
                                    var status = actor.getString("favorite")
                                    var Date1 = actor.getString("createdAt")
                                    for (j in 0..3)
                                        year1.append(Date1[j])
                                    for (j in 5..6)
                                        month1.append(Date1[j])
                                    for (j in 8..9)
                                        date1.append(Date1[j])
                                    for (j in 11..12)
                                        hours1.append(Date1[j])
                                    for (j in 14..15)
                                        min1.append(Date1[j])
                                    Log.d("Date is ", "$date1 $month1 $year1  $hours1: $min1")
                                    var temp = month1.toString().toInt()
                                    statusDate1 = "$date1 ${monthName[--temp]} $year1"
                                    Log.d("Date", "Status Date is $statusDate1")
                                    var tempHours = hours1.toString().toInt()
                                    var tempMinutes = min1.toString().toInt()
                                    if (tempHours == 12 && tempMinutes == 0) {
                                        Log.d("Time", "Time is ${tempHours}:${tempMinutes} noon")
                                        statusTime1 = "${tempHours}:${tempMinutes} noon"
                                    } else if (tempHours > 12) {
                                        tempHours = tempHours - 12
                                        Log.d("Time", "Time is ${tempHours}:${tempMinutes} PM")
                                        statusTime1 = "${tempHours}:${tempMinutes} PM"
                                    } else {
                                        Log.d("Time", "Time is ${tempHours}:${tempMinutes} AM")
                                        statusTime1 = "${tempHours}:${tempMinutes} AM"
                                    }

                                    listOfQuote.add(PojoAllQuoteDetailDashBoard(id, text, background, status, statusDate1!!, statusTime1!!))
                                }
                                for (i in 0 until listOfQuote.size) {
                                    var item: PojoAllQuoteDetailDashBoard = listOfQuote.get(i)
                                    Log.d("Entry ${i}", "${item.id}  ${item.text}  ${item.background}")

                                }
                                var session = Session(context?.applicationContext!!)
                                var adapter = CustomAdpater(listOfQuote, contextD!!, object : CustomAdpater.OnViewClicked {
                                    override fun onClick(position: Int) {
                                        Log.d("Clicked", "yes ${position}")
                                        var obi = LoginDataUser.instance
                                        var item: PojoAllQuoteDetailDashBoard = listOfQuote.get(position)
                                        obi.QuoteID = item.id
                                        pos = position
                                        //Photo_box(context)
                                    }
                                })
                                var manager = GridLayoutManager(context, 1)
                                recyclerview?.layoutManager = manager
                                recyclerview?.adapter = adapter
                            }


                        } catch (e: JSONException) {
                            var use = ReuseMethod()
                            //use.showSnakBar(getview,"JSONException")
                            Log.e("JSONException", "onResponse: JSON Exception: ");
                        } catch (e: IOException) {
                            var use = ReuseMethod()
                            // use.showSnakBar(getview,"JSON Exception")
                            Log.e("IOexception", "onResponse: JSONException: ");
                        }

                    }

                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                        Log.e("OnFailure", "onFailure: Something went wrong: ");
                        var use = ReuseMethod()
                        //use.showSnakBar(getview,"Something went wrong")
                        //Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
                    }
                })
                LoginDataUser.instance.count++
            }
            var adapter = CustomAdpater(listOfQuote, contextD!!, object : CustomAdpater.OnViewClicked {
                override fun onClick(position: Int) {
                    Log.d("Clicked", "yes ${position}")
                    var obi = LoginDataUser.instance
                    var item: PojoAllQuoteDetailDashBoard = listOfQuote.get(position)
                    obi.QuoteID = item.id
                    pos = position
                    //Photo_box(context)
                }
            })
            var manager = GridLayoutManager(context, 1)
            recyclerview?.layoutManager = manager
            recyclerview?.adapter = adapter

        view?.home?.setOnClickListener {
            Log.d("String", "Yes")
            mDrawer.closeDrawer(Gravity.LEFT)
        }
        view?.Reflection?.setOnClickListener {
            val transaction = fragmentManager?.beginTransaction()
            transaction?.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right)
            var fragmentLogin = fragmentSideMenuReflection()
            transaction?.replace(R.id.container, fragmentLogin)
            transaction?.addToBackStack("Dashboard Page")
            transaction?.commit()
        }
        view?.favourites?.setOnClickListener {
            var transaction = fragmentManager?.beginTransaction()
            transaction?.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right)
            var fragmentLogin = fragmentFavourite()
            transaction?.replace(R.id.container, fragmentLogin)
            transaction?.addToBackStack("Dashboard Page")
            transaction?.commit()
        }
        view?.MantraMePremium?.setOnClickListener {
            //val transaction = fragmentManager?.beginTransaction()
            //transaction?.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right)
            //var fragmentLogin = fragmentPremium()
            //transaction?.replace(R.id.container, fragmentLogin)
            //transaction?.addToBackStack("Dashboard Page")
            //transaction?.commit()
            var use=ReuseMethod()
            use.showSnakBar(it,"Coming Soon")
        }
        view?.Alarm?.setOnClickListener {
            Alarm_box(it.context)
        }
        view?.ShareThisApp?.setOnClickListener {
            shareIt()
        }
        view?.settings?.setOnClickListener {
            //val transaction = fragmentManager?.beginTransaction()
            //transaction?.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right)
            //var fragmentLogin = fragmentSideMenuSettings()
            //transaction?.replace(R.id.container, fragmentLogin)
            //transaction?.addToBackStack("Dashboard Page")
            //transaction?.commit()
            //var Loading=Loader()
            //Loading.ComingSoon(it.context)
            var use=ReuseMethod()
            use.showSnakBar(it,"Coming Soon")
        }
        view?.logouts?.setOnClickListener {
            val builder = AlertDialog.Builder(it.context)
            builder.setTitle("Logout")
            builder.setMessage("Are you sure you want to logout?")
            builder.setPositiveButton("YES") { dialog, which ->
                var manager = fragmentManager
                while (manager!!.backStackEntryCount > 0) {
                    manager.popBackStackImmediate()
                }
                var session = Session(context?.applicationContext!!)
                session.setLoginStatus()
                session.setLoggedin(false," "," "," "," "," ")
                session.removeAll()
                val transaction = fragmentManager?.beginTransaction()
                //transaction?.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right)
                var fragmentLogin = fragmentLogin()
                transaction?.replace(R.id.container, fragmentLogin)
                transaction?.commit()
                dialog.dismiss()
            }

            builder.setNegativeButton("NO") { dialog, which ->
                dialog.dismiss()
            }

            val alert = builder.create()
            alert.show()
        }
    }

    fun Alarm_box(view: Context) {

        var hours: Int? = null
        var minutes: Int? = null
        var builder: AlertDialog.Builder = AlertDialog.Builder(context)
        var inflater: LayoutInflater = layoutInflater
        var alarmView: View = inflater.inflate(R.layout.fragment_alarm, null)
        builder.setView(alarmView)
        var Dilog: Dialog = builder.create()
        Dilog.show()
        alarmView.DatePickerAlarm.setStepMinutes(1)
        alarmView.DatePickerAlarm.addOnDateChangedListener(SingleDateAndTimePicker.OnDateChangedListener
        { displayed, date ->
            Log.d("Value is ", "${date.hours}   ${date.minutes}")
            if (date.minutes > -1 && date.minutes < 10) {
                alarmView.clockAlarm.text = "${date.hours}:0${date.minutes}"
            } else {
                alarmView.clockAlarm.text = "${date.hours}:${date.minutes}"
            }

            hours = date.hours
            minutes = date.minutes
            Log.d("Date", "${date.date}")
            Log.d("Month", "${date.month}")
            Log.d("Year", "${date.year}")


        }
        )
        alarmView.CancelAlarm.setOnClickListener {
            Dilog.dismiss()
        }
        alarmView.okAlarm.setOnClickListener {
            Toast.makeText(it.context, "Time is ${alarmView.DatePickerAlarm.date.hours}", Toast.LENGTH_SHORT).show()
            Log.d("Data", "Time is ${hours} : ${minutes}")
            var SaveData = com.developer.rohal.mantra.AlarmManager(it.context)
            SaveData.SaveData(hours!!, minutes!!)
            SaveData.SetAlarm()
            Dilog.dismiss()
        }
    }

    private fun shareIt() {
        val sharingIntent = Intent(android.content.Intent.ACTION_SEND)
        sharingIntent.setType("text/plain");
        val shareBody = "Here is the share content body"
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject Here");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
        context?.startActivity(Intent.createChooser(sharingIntent, "Share via"));
    }

    fun Photo_box(view: Context?) {
        var builder: AlertDialog.Builder = AlertDialog.Builder(context)
        var inflater: LayoutInflater = layoutInflater
        var view: View = inflater.inflate(R.layout.photo_box, null)
        builder.setView(view)
        var Dilog: Dialog = builder.create()
        Dilog.show()
        view.btn_Gallery.setOnClickListener {
            gallery()
            builder.setInverseBackgroundForced(true)
            Dilog.dismiss()
            //Show_box(context)
        }
        view.btn_Camera.setOnClickListener {
            dispatchTakePictureIntent()
            Dilog.dismiss()
            //Show_box(context)
        }

    }

    private fun dispatchTakePictureIntent() {
        var takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (takePictureIntent.resolveActivity(context?.packageManager) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
        }
    }

    fun gallery() {
        var intent = Intent(
                Intent.ACTION_PICK,
                MediaStore.Images.Media.INTERNAL_CONTENT_URI)


        Log.d("String", "Yes")
        startActivityForResult(intent, PICK_FROM_FILE)
        Log.d("String", "YesYes")
    }

    fun Show_box(view: Context?) {
        var builder: AlertDialog.Builder = AlertDialog.Builder(context)
        var inflater: LayoutInflater = layoutInflater
        var view1: View = inflater.inflate(R.layout.show_box, null)
        builder.setView(view1)
        var Dilog: Dialog = builder.create()
        Dilog.show()
        //var ob: BitmapDrawable = BitmapDrawable(getResources(), bitmap)
        //view1.makeImageCirculartest.background = ob
        view1.makeImageCirculartest.setImageBitmap(bitmap)
        var item: PojoAllQuoteDetailDashBoard = listOfQuote.get(pos!!)
        view1.MakeTexttest.setText(item.text)
        view1.testok.setOnClickListener {
            var loader = Loader()
            loader.ShowCustomLoader(it.context)
            var client = ApiCall()
            var retrofit = client.retrofitClient()
            val redditAP = retrofit?.create(RedditAPI::class.java)
            val headerMap = HashMap<String, RequestBody>()
            var obi = LoginDataUser.instance
            Log.d("String", "${obi.QuoteID}")
            headerMap.put("quoteID", RequestBody.create(MediaType.parse("text/plain"), obi.QuoteID))
            var query: String = "background\";filename=\"${FilePicture().absolutePath}"
            headerMap.put(query, RequestBody.create(MediaType.parse("/Images"), FilePicture()))           ///
//            var ob=LoginDataUser.instance
            var use = ReuseMethod()
//            Log.d("data","${ob.token}")
            var call = redditAP?.makeImage(obi.token!!, headerMap)
            call?.enqueue(object : Callback<ResponseBody> {
                override fun onResponse(call: Call<ResponseBody>, response: retrofit2.Response<ResponseBody>?) {
                    Log.d("server back", "onResponse: Server Response: " + response.toString());

                    try {
                        if (response?.isSuccessful!!) {
                            LoginDataUser.instance.count=1
                            val json: String = response?.body()!!.string();
                            Log.d("JSON back", "onResponse: json: " + json);
                            var data: JSONObject? = null;
                            data = JSONObject(json)
                            data=data.getJSONObject("data")
                            var status = data.get("success")
                            LoginDataUser.instance.count=0
                            Log.d("StatusBar back", "${status}")
                            if (status == 0) {

                                use.showSnakBar(it, "User Exists Error")
                                loader.HideCustomLoader()
                            } else {
                                use.showSnakBar(it, "Successfully Background Image Change!!")
                                loader.HideCustomLoader()
                                Dilog.dismiss()
                            }
                        }
                    } catch (e: JSONException) {
                        Log.e("JSONException", "onResponse: JSONException: ");
                        use.showSnakBar(it, "JSONException")
                        loader.HideCustomLoader()
                    } catch (e: IOException) {
                        Log.e("IOexception", "onResponse: JSONException: ");
                        use.showSnakBar(it, "JSONException")
                        loader.HideCustomLoader()
                    }

                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Log.e("OnFailure", "onFailure: Something went wrong: ");
                    use.showSnakBar(it, "Internet connection problem")
                    loader.HideCustomLoader()
                    Dilog.dismiss()
                }
            })
        }
        view1.testCancel.setOnClickListener {
            Dilog.dismiss()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        Log.d("yes", "yesyesyesyes")

        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_IMAGE_CAPTURE) {
            var extras = data?.extras
            bitmap = extras?.get("data") as Bitmap
            bmpuri = bitmap
            var file: File? = null
            file = FilePicture(bitmap!!)
            if (file == null) {
                Log.d("uploading", "file is null")
            } else {
                Show_box(context)       ///

            }

        } else if (requestCode == PICK_FROM_FILE && resultCode == AppCompatActivity.RESULT_OK) {
            try {
                var mCapturedImageURI: Uri = data?.data!!
                bitmap = MediaStore.Images.Media.getBitmap(context?.getContentResolver(), mCapturedImageURI);
                bmpuri = bitmap
                var file: File? = null
                file = FilePicture(bitmap!!)
                if (file == null) {
                    Log.d("uploading", "file is null")
                } else {
                    Show_box(context)
                }
            } catch (e: java.lang.Exception) {

                e.printStackTrace()
                // Toast.makeText(context, "Image_notfound ${e}", Toast.LENGTH_LONG).show()
            }

        }

    }

    fun FilePicture(bitmap: Bitmap): File {
        var f: File = File(context?.cacheDir, "${bitmap.toString()}")
        f.createNewFile()
        var bos: ByteArrayOutputStream = ByteArrayOutputStream();
        bitmap?.compress(Bitmap.CompressFormat.PNG, 0 /*ignored for PNG*/, bos);
        var bitmapdata = bos.toByteArray();
        var fos: FileOutputStream = FileOutputStream(f);
        fos.write(bitmapdata);
        fos.flush();
        fos.close();
        return f
    }

    fun FilePicture(): File {
        var f: File = File(context?.cacheDir, "Saii.png")
        f.createNewFile()
//Convert bitmap to byte array
        var bitmap: Bitmap? = bmpuri
        var bos: ByteArrayOutputStream = ByteArrayOutputStream();
        bitmap?.compress(Bitmap.CompressFormat.PNG, 0 /*ignored for PNG*/, bos);
        var bitmapdata = bos.toByteArray();

//write the bytes in file
        var fos: FileOutputStream = FileOutputStream(f);
        fos.write(bitmapdata);
        fos.flush();
        fos.close();
        return f
    }

    fun onBack() {
        Log.d("onback", "yes")
        val alertDilog = AlertDialog.Builder(context).create()
        alertDilog.setTitle("Alert")
        alertDilog.setMessage("Are you sure You want to Exit?")
        alertDilog.setButton(AlertDialog.BUTTON_POSITIVE, "YES", { dialogInterface, i ->

            val fragmentManager = fragmentManager
            // or 'getSupportFragmentManager();'
            val count = fragmentManager!!.backStackEntryCount
            for (i in 0 until count) {
                fragmentManager.popBackStack()
            }


            android.os.Process.killProcess(android.os.Process.myPid());

            alertDilog.cancel();
        })
        alertDilog.setButton(AlertDialog.BUTTON_NEGATIVE, "NO", { dialogInterface, i ->
            alertDilog.cancel();
        })
        alertDilog.show()
        return
    }
    fun hitApi()
    {

    }
}



