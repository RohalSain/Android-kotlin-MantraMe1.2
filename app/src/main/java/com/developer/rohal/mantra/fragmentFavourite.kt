package com.developer.rohal.mantra


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import kotlinx.android.synthetic.main.fragment_fragment_dashboard.*
import kotlinx.android.synthetic.main.fragment_fragment_favourite.*
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
import android.R.array




/**
 * A simple [Fragment] subclass.
 */
class fragmentFavourite : Fragment() {
    var listOfJSONObject:ArrayList<JSONObject> =ArrayList<JSONObject>();
    var listOfQuote:ArrayList<PojoAllQuoteDetail> = ArrayList<PojoAllQuoteDetail>();

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Log.d("Show","yes")
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
        var obj=LoginDataUser.instance
        val redditAP = retrofit.create(RedditAPI::class.java)
        var call = redditAP.GetAllLikeQuote(obj.token!!)
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: retrofit2.Response<ResponseBody>?) {
                Log.d("server", "onResponse: Server Response: " + response.toString());

                try {
                    if(listOfQuote.size!=0)
                    {
                        listOfQuote.clear()
                    }
                    var json:String?=null
                    json= response?.body()?.string()
                    if (json==null) {
                        Log.d("Get Quote", "no")
                    }
                    else {
                        Log.d("Api for favourite","yes")
                        Log.d("JSON Get Quote", "onResponse: json: " + json);
                        var data: JSONObject? = null;
                        data = JSONObject(json);
                        Log.d("Json Object","${data}")
                        var cast: JSONArray = data.getJSONArray("data");
                        Log.d("Length is ","${cast.length()}")
                        for (n in 0 until cast.length()) {
                            var obj = cast.getJSONObject(n)
                            var data1=obj.getJSONObject("quote")
                            Log.d("Data is ","${data1}")
                            var id = data1.getString("_id")
                            var text = data1.getString("text")
                            var background=data1.getString("background")
                            var status=data1.getString("favorite")
                            Log.d("get data","${id} ${text} ${background} ${status}")
                            listOfQuote.add(PojoAllQuoteDetail(id,text,background,status))
                            // do some stuff....
                        }
                        /*
                        var cast: JSONArray = data.getJSONArray("data");
                        Log.d("Quotes are","${cast}")
                        for (i in 0 until cast.length())
                        {
                            var cast1: JSONArray = cast.getJSONArray("quote");
                            Log.d("yes","yes")
                            val actor = cast1.getJSONObject(i)
                            Log.d("yes","yes")
                            var id = cast1.getString("_id")
                            Log.d("yes","yes")
                            //var text = actor.getString("text")
                            Log.d("yes","yes")
                            var background=cast1.getString("background")
                            Log.d("yes","yes")
                            var status=cast1.getString("favorite")
                            Log.d("yes","yes")
                            listOfQuote.add(PojoAllQuoteDetail(id,"yes","yes",status))
                        }
                        for(i in 0 until listOfQuote.size )
                        {
                            var item:PojoAllQuoteDetail=listOfQuote.get(i)
                            Log.d("Entry favoirite  ${i}","${item.id }  ${item.text }  ${item.background }")

                        }
                        */
                        var adapter:CustomAdpaterFavourite?=null
                        adapter= CustomAdpaterFavourite(listOfQuote, activity?.baseContext!!)
                        var manager= GridLayoutManager(context,1)
                        recyclerviewfavourite.layoutManager = manager
                        recyclerviewfavourite.adapter = adapter
                        //var adapter = CustomAdpater(listOfQuote, contextD!!)
                        //var manager = GridLayoutManager(context, 1)
                        //recyclerview?.layoutManager = manager
                        //recyclerview?.adapter = adapter

//                            Log.d(TAG, "onResponse: data: " + data.optString("json"));

                        //Log.d("detial","Emial is ${emial} First Name ${firstName} Phone Number  ${phoneNumber} User Pic is ${pic} Country is ${country}")
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
        /*
        var list: ArrayList<User> = ArrayList()
        //var titleFont = Typeface.createFromAsset(context.assets,"fonts/BubblegumSans-Regular.ttf")
        //name.setTypeface(titleFont)
        list.add(User(R.drawable.three,"23 Feb 2017", "3.45PM"))
        list.add(User(R.drawable.one,"24 Feb 2017", "4.45PM"))
        list.add(User(R.drawable.three,"25 Feb 2017", "5.45PM"))
        list.add(User(R.drawable.three,"23 Feb 2017", "3.45PM"))
        list.add(User(R.drawable.one,"24 Feb 2017", "4.45PM"))
        list.add(User(R.drawable.three,"25 Feb 2017", "5.45PM"))
        //var adapter = CustomAdpaterFavourite(list, this!!.context!!)
        var manager= GridLayoutManager(context,1)
        recyclerviewfavourite.layoutManager = manager
        recyclerviewfavourite.adapter = adapter */

        FavouriteBackButton.setOnClickListener {
            activity?.onBackPressed()
        }
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_fragment_favourite, container, false)
    }

}




