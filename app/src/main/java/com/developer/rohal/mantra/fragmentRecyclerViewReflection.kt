package com.developer.rohal.mantra


import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.android.synthetic.main.frgament_recycler_view_reflection.*
import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.ResponseBody
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.util.HashMap
import java.util.concurrent.TimeUnit


/**
 * A simple [Fragment] subclass.
 */
class fragmentRecyclerViewReflection : Fragment() {
    private val SPLASH_TIME_OUT = 4000
     var loader=Loader()
     var use=ReuseMethod()
     var QuoteID:String?=null
     lateinit var currentFragment:fragmentRecyclerViewReflection

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.frgament_recycler_view_reflection, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        currentFragment=this

        fragment.setOnClickListener {
            it.hideKeyboard()
        }
        Refback.setOnClickListener {
            activity?.onBackPressed()
        }
        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
        var obj=LoginDataUser.instance
        Log.d("Display","Quote: ${obj.Quotes} Background: ${obj.BackGround}")
        addReflectionRecyclerView.setImageURI(Uri.parse("http://139.59.18.239:6010/mantrame/${obj.BackGround}"))
        text.setText(Html.fromHtml("${obj.Quotes}"))
        Refdel.setOnClickListener {
            Toast.makeText(context,"You hace clicked on delete button",Toast.LENGTH_SHORT).show()
        }
        Refok.setOnClickListener {
            if(addReflectiontext.text.isEmpty() || addReflectiontext.text.trim().length==0)
            {
                var use= ReuseMethod()
                use.showSnakBar(it,"Empty Reflection")
                loader.HideCustomLoader()
           }
            else
            {
                var loading=Loader()
                loading.ShowCustomLoader(it.context)
                var reflection=addReflectiontext.text.toString()
                var client=ApiCall()
                var retrofit=client.retrofitClient()
                val redditAP = retrofit?.create(RedditAPI::class.java)
                var obj = LoginDataUser.instance
                Log.d("reflection","${reflection}")
                //var data=PojaReflectionData(obj.QuoteID,reflection)
                var call = redditAP?.addReflection(obj.token!!,obj.QuoteID,reflection)
                call?.enqueue(object : Callback<ResponseBody> {
                    override fun onResponse(call: Call<ResponseBody>, response: retrofit2.Response<ResponseBody>?) {
                        Log.d("server", "onResponse: Server Response: " + response.toString());

                        try {
                            var json:String?=null
                            json= response?.body()?.string()
                            if (json==null) {
                                Log.d("String", "yes")
                                use.showSnakBar(it,"Error Occur")

                            }
                            else {
                                Log.d("JSON", "onResponse: json: " + json);
                                var data1: JSONObject? = null;
                                data1 = JSONObject(json);
                                Log.d("token ","${data1}")
                                var status=data1.get("success")
                                Log.d("Data Get","${status}")
                                    use.showSnakBar(it,"Sucessfully Reflection Change")
                                    loading.HideCustomLoader()
                                activity?.onBackPressed()

                            }


                        } catch (e: JSONException) {
                            Log.e("JSONException", "onResponse: JSONException: ");

                                loading.HideCustomLoader()

                            use.showSnakBar(it,"JSON Exception")


                        } catch (e: IOException) {
                            Log.e("IOexception", "onResponse: JSONException: ");
                            use.showSnakBar(it,"JSON Exception")
                                loading.HideCustomLoader()



                        }

                    }

                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                        Log.e("OnFailure", "onFailure: Something went wrong: ")
                            loading.HideCustomLoader()

                        var use=ReuseMethod()
                        use.showSnakBar(it,"Please Check Internet Connection")
                        //Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
                    }
                })
            }
            }
        favouriteRecyclerView.setOnClickListener {
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
            val redditAP = retrofit.create(RedditAPI::class.java)
            var obi=LoginDataUser.instance
            var call = redditAP.GetMark(obi.token!!,obi.QuoteID)
            call.enqueue(object : Callback<ResponseBody> {
                override fun onResponse(call: Call<ResponseBody>, response: retrofit2.Response<ResponseBody>?) {
                    Log.d("server", "onResponse: Server Response: " + response.toString());

                    try {
                        var json:String?=null
                        json= response?.body()?.string()
                        if (json==null) {
                            use.showSnakBar(it,"Error Occured")
                            Log.d("String", "yes")
                        }
                        else {
                            Log.d("fav", "onResponse: json: " + json);
                            var data2: JSONObject? = null;
                            data2 = JSONObject(json);
                            Log.d("Favourite ","${data2}")
                            var message = data2.getString("message")
                            Log.d("Status ","${message}")
                            if(message.equals("Favorite removed successfully"))
                            {
                                Log.d("favourite remove","yes")
                                var imgResource = R.drawable.ic_favorite_grey
                                favouriteRecyclerView.setCompoundDrawablesWithIntrinsicBounds(imgResource, 0, 0, 0)
                                use.showSnakBar(it,"Favorite removed successfully")
                            }
                            else
                            {
                                Log.d("favourite add","yes")
                                var imgResource = R.drawable.ic_selected_favorite_white
                                favouriteRecyclerView.setCompoundDrawablesWithIntrinsicBounds(imgResource, 0, 0, 0)
                                use.showSnakBar(it,"Favourite ")
                            }
                        }


                    } catch (e: JSONException) {
                        Log.e("JSONException", "onResponse: JSONException: ")
                        use.showSnakBar(it,"JSON Exception ")
                    } catch (e: IOException) {
                        Log.e("IOexception", "onResponse: JSONException: ")
                        use.showSnakBar(it,"JSON Exception ")
                    }

                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Log.e("OnFailure", "onFailure: Something went wrong: ")
                    //Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
                    use.showSnakBar(it,"Something went wrong ")
                }
            })
        }
        ShareRecylerView.setOnClickListener {
            shareIt()
        }
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        var loading=Loader()
        loading.ShowLoader(view.context)
        var reflection=addReflectiontext.text.toString()
        var client=ApiCall()
        var retrofit=client.retrofitClient()
        val redditAP = retrofit?.create(RedditAPI::class.java)
        var obj1 = LoginDataUser.instance
        Log.d("Detail","${obj1.token} ${obj1.QuoteID}")
        var call = redditAP?.getReflection(obj1.token!!,obj1.QuoteID)
        call?.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: retrofit2.Response<ResponseBody>?) {
                Log.d("reflection", "onResponse: Server Response: " + response.toString());
                try {
                    var json:String?=null
                    json= response?.body()?.string()
                    if (json==null) {
                        Log.d("reflection String ", "yes")
                        Handler().postDelayed(Runnable
                        {
                            val transaction = fragmentManager?.beginTransaction()
                            transaction?.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right)
                            var fragmentLogin = fragmentLogin()
                            transaction?.replace(R.id.container, fragmentLogin)
                            transaction?.commit()
                        }, SPLASH_TIME_OUT.toLong())
                        loading.HideLoader()
                    }
                    else {
                        Log.d("reflection JSON", "onResponse: json: " + json);
                        var data: JSONObject? = null;
                        data = JSONObject(json);
                        var status=data.get("success")
                        Log.d("reflection Data Get","${status}")
                        var reflectiondata=data.getJSONObject("data")
                        var reff=reflectiondata.get("reflection")
                        if(reff==null)
                        {

                        }
                        else
                        {
                            try {
                                addReflectiontext.setText("${reff.toString()}",TextView.BufferType.EDITABLE)
                            }
                            catch (e:Exception)
                            {

                            }

                        }
                        loading.HideLoader()
                    }


                } catch (e: JSONException) {
                    Log.e("JSONException", "onResponse: JSONException: ");
                    loading.HideLoader()
                } catch (e: IOException) {
                    Log.e("IOexception", "onResponse: JSONException: ");
                    loading.HideLoader()
                }

            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.e("OnFailure", "onFailure: Something went wrong: ")
                var use=ReuseMethod()
                //Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        })
    }
    private fun shareIt() {
        val sharingIntent = Intent(Intent.ACTION_SEND)
        sharingIntent.setType("text/plain");
        val shareBody = "Here is the share content body"
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject Here");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
        context?.startActivity(Intent.createChooser(sharingIntent, "Share via"));
    }
    fun View.hideKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }
}
