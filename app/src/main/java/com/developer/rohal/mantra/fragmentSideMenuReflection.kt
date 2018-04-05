package com.developer.rohal.mantra


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.StaggeredGridLayoutManager
import android.util.Log
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import kotlinx.android.synthetic.main.fragment_side_menu_reflection.*
import android.widget.Toast
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import org.json.JSONArray
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
class fragmentSideMenuReflection : Fragment() {
    private val SPLASH_TIME_OUT = 2000
    var item: PojoAllQuoteDetailReflection?=null
    var listOfQuote:ArrayList<PojoAllQuoteDetailReflection> = ArrayList<PojoAllQuoteDetailReflection>();
    var madpater:RecyclerViewClickListener?=null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_side_menu_reflection, container, false)
    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        fragmentSideMenu.setOnClickListener {
            it.hideKeyboard()
        }
        // api hit
        try {


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
            var call = redditAP.getAllReflection("${obj.token}")
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
                        } else {
                            Log.d("Api", "yes")
                            Log.d("JSON Get Quote", "onResponse: json: " + json);
                            var data: JSONObject? = null;
                            data = JSONObject(json);
                            var cast: JSONArray = data.getJSONArray("data");
                            Log.d("Array of Reflection", "${cast}")
                            for (i in 0 until cast.length()) {
                                val actor = cast.getJSONObject(i)
                                var ref=actor.get("reflection")
                                var quote=actor.getJSONObject("quote")
                                var id = quote.getString("_id")
                                var text =quote.getString("text")
                                var status = quote.getString("favorite")
                                var background = quote.getString("background")
                                listOfQuote.add(PojoAllQuoteDetailReflection(id, text, background, status,ref.toString()))
                            }
                            for (i in 0 until listOfQuote.size) {
                                var items: PojoAllQuoteDetailReflection = listOfQuote.get(i)
                                Log.d("Entry ${i}", "${items.id}  ${items.text}  ${items.background}")
                                if (i == 0) {
                                    try {
                                        Quote.setText(" ")
                                        Quote.setText("${items.text}")
                                        item=items
                                        image.setImageURI("http://139.59.18.239:6010/mantrame/${items.background}?dim=500x500")
                                        addReflection.setText("${items.reflection}",TextView.BufferType.EDITABLE)
                                    } catch (e: Exception) {

                                    }

                                }

                            }
                            val staggeredGridLayoutManager = StaggeredGridLayoutManager(
                                    1, //The number of Columns in the grid
                                    LinearLayoutManager.HORIZONTAL)
                            try {

                                recyclerviewSideMenu.layoutManager = staggeredGridLayoutManager
                            }
                            catch (e:Exception)
                            {

                            }
                                var adapter = CustomAdpaterSideMenuReflection(listOfQuote, activity?.applicationContext!!, object : CustomAdpaterSideMenuReflection.OnItemClickListener {
                                override fun onItemClick(position: Int) {
                                    Log.d("Clicked ha ", "yes")
                                    item= listOfQuote.get(position)
                                    Quote.setText(" ")
                                    Quote.setText("${item?.text}")
                                    image.setImageURI("http://139.59.18.239:6010/mantrame/${item?.background}?dim=500x500}")
                                    addReflection.setText("${item?.reflection}",TextView.BufferType.EDITABLE)
                                }
                            })
                            recyclerviewSideMenu.adapter = adapter
                        }


                    } catch (e: JSONException) {
                        Log.e("JSONException", "onResponse: JSONException: ");
                    } catch (e: IOException) {
                        Log.e("IOexception", "onResponse: JSONException: ");
                    }

                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Log.e("OnFailure", "onFailure: Something went wrong: ");
                    Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            })
        }
        catch (e:Exception)
        {

        }

        //
        RefCancel.setOnClickListener {
            activity?.onBackPressed()
        }
        Refdel.setOnClickListener {
            var snackbar1: Snackbar = Snackbar.make(it, "Delete Button Pressed!", Snackbar.LENGTH_SHORT);
            snackbar1.show();
        }
        Refok.setOnClickListener {
            if (listOfQuote.size == 0) {
                var use = ReuseMethod()
                use.showSnakBar(it, "No Reflection added yet")
            } else {
                var useme = ReuseMethod()
                var loader = Loader()
                loader.ShowCustomLoader(it.context)
                var client = ApiCall()
                var retrofit = client.retrofitClient()
                var ref = addReflection.text.toString()
                val redditAP = retrofit?.create(RedditAPI::class.java)
                var obj = LoginDataUser.instance
                Log.d("value are", "id ${obj.QuoteID}  token ${obj.token} ref  ${ref}")
                var call = redditAP?.addReflection(obj.token!!, item?.id!!, "${ref}")
                call?.enqueue(object : Callback<ResponseBody> {
                    override fun onResponse(call: Call<ResponseBody>, response: retrofit2.Response<ResponseBody>?) {
                        Log.d("server", "onResponse data: Server Response: " + response.toString());

                        try {
                            var json: String? = null
                            json = response?.body()?.string()
                            if (json == null) {
                                Log.d("String data", "yes")
                                useme.showSnakBar(it, "Error Occur")
                                Handler().postDelayed(Runnable
                                {
                                    activity?.onBackPressed()
                                }, SPLASH_TIME_OUT.toLong())
                            } else {
                                Log.d("JSON data", "onResponse: json: " + json);
                                var data1: JSONObject? = null;
                                data1 = JSONObject(json);
                                Log.d("token ", "${data1}")
                                var status = data1.get("success")
                                Log.d("Data Get", "${status}")
                                useme.showSnakBar(it, "Sucessfully Reflection Change")
                                loader.HideCustomLoader()
                                Handler().postDelayed(Runnable
                                {
                                    activity?.onBackPressed()
                                }, SPLASH_TIME_OUT.toLong())
                            }


                        } catch (e: JSONException) {
                            Log.e("JSONException", "onResponse: JSONException: ");

                            loader.HideCustomLoader()

                            useme.showSnakBar(it, "JSON Exception")
                            Handler().postDelayed(Runnable
                            {
                                activity?.onBackPressed()
                            }, SPLASH_TIME_OUT.toLong())

                        } catch (e: IOException) {
                            Log.e("IOexception", "onResponse: JSONException: ");
                            useme.showSnakBar(it, "JSON Exception")

                            loader.HideCustomLoader()
                            Handler().postDelayed(Runnable
                            {
                                activity?.onBackPressed()
                            }, SPLASH_TIME_OUT.toLong())


                        }

                    }

                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                        Log.e("OnFailure", "onFailure: Something went wrong: ")
                        loader.HideCustomLoader()
                        activity?.onBackPressed()

                        var use = ReuseMethod()
                        use.showSnakBar(it, "Please Check Internet Connection")
                        //Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
                        activity?.onBackPressed()
                    }
                })
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
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