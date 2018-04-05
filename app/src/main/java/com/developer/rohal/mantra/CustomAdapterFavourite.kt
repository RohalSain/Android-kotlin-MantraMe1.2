package com.developer.rohal.mantra
import android.annotation.TargetApi

import android.content.Context
import android.graphics.Typeface
import android.net.Uri
import android.support.v7.widget.RecyclerView
import kotlinx.android.synthetic.main.items.view.*
import android.os.Build
import android.text.Html
import android.util.Log
import android.view.*
import android.widget.PopupMenu
import android.support.v4.app.FragmentActivity
import kotlinx.android.synthetic.main.itemsfavourite.view.*
import okhttp3.ResponseBody
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import java.io.IOException
import kotlin.collections.ArrayList


class CustomAdpaterFavourite(var userlist: ArrayList<PojoAllQuoteDetail>, var context: Context): RecyclerView.Adapter<CustomAdpaterFavourite.ViewHolder>()
{
    var i=0;
    @TargetApi(Build.VERSION_CODES.M)
    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        holder?.Reflection?.setOnClickListener {
            Log.d("Changing","ok Report")
            val transaction = (context as FragmentActivity).supportFragmentManager.beginTransaction()
            //transaction?.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right)
            var fragmentLogin = fragmentRecyclerViewReflection()
            transaction?.replace(R.id.container, fragmentLogin)
            transaction?.addToBackStack("Dashboard Page")
            transaction?.commit()

        }
        /*
        val user:User=userlist[position]
       // holder?.quotePic?.setImageResource(user.src)
        holder?.setting?.setOnClickListener {
            Toast.makeText(it.context,"Setting", Toast.LENGTH_SHORT).show()
        }
        holder?.favourite?.setOnClickListener {
            Toast.makeText(it.context,"Favourite",Toast.LENGTH_SHORT).show()
        }
        holder?.Reflection?.setOnClickListener {
            Toast.makeText(it.context,"Copy",Toast.LENGTH_SHORT).show()
        }
         holder?.share?.setOnClickListener {
            Toast.makeText(it.context,"Save",Toast.LENGTH_SHORT).show()
        }
      */
        /*
       holder?.setting?.setOnClickListener {
           var builder: AlertDialog.Builder = AlertDialog.Builder(it.context)
           val inflater = LayoutInflater.from(it.context)
           var view: View = inflater.inflate(R.layout.setting_dialog_box, null)
           builder.setView(view)
           var Dilog: Dialog = builder.create()
           val wmlp = Dilog.getWindow().getAttributes()
           wmlp.gravity = Gravity.TOP or Gravity.LEFT
           wmlp.x =  it.top-10
           Log.d("Data","${it.top}")
           wmlp.y = it.right
           Dilog.show()
           view.btnMakeImage.setOnClickListener {
           }
           */
        holder?.setting?.setOnClickListener {
            val popup = PopupMenu(it.context, it)
            popup.inflate(R.menu.options)
            if(Build.VERSION.SDK_INT>22)
            {
                Log.d("Version","${Build.VERSION.SDK_INT}")
                popup.gravity=Gravity.END
            }
            popup.setOnMenuItemClickListener(object : PopupMenu.OnMenuItemClickListener {
                override fun onMenuItemClick(item: MenuItem): Boolean {
                    when (item.itemId) {
                        R.id.makeImage -> {
                            Log.d("String", "Yes Yes");
                        }
                    }
                    return false
                }
            })
            popup.show()
        }
        holder?.favourite?.setOnClickListener {
            removeItem(holder.adapterPosition,it.context)

        }
    }

    override fun getItemCount(): Int {
        return userlist.size
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder? {
        val v = LayoutInflater.from(parent?.context).inflate(R.layout.itemsfavourite, parent, false)
        var titleFont = Typeface.createFromAsset(parent?.context?.assets, "fonts/BubblegumSans-Regular.ttf")
        v.Sharekey.setTypeface(titleFont)
        v.Reflectionkey.setTypeface(titleFont)
        v.favouritekey.setTypeface(titleFont)
        //
        var item:PojoAllQuoteDetail=userlist.get(i)
        Log.d("Data","${userlist.size}")
        Log.d("Count",i.toString())
        Log.d("View",viewType.toString())
        if(item.like=="1")
        {
            Log.d("like","yes")
            val imgResource = R.drawable.ic_selected_favorite_white
            //v.favourite.setCompoundDrawablesWithIntrinsicBounds(imgResource, 0, 0, 0)
        }
        //v.text.setText(Html.fromHtml("Accept your past without regret. <br> Handle your present with confidence. <br> Face your future without fear.").toString())
        v.textFavourite.setText(Html.fromHtml("${item.text}"))
        v.dashBoardRecyclerViewBackgroundFavourite.setImageURI(Uri.parse("http://139.59.18.239:6010/mantrame/${item.background}?dim=500x500"))

        //
        i++
        return ViewHolder(v)
    }
    class ViewHolder(itemView: View) :RecyclerView.ViewHolder(itemView){
      /*
        //var quotePic: ImageView = itemView.imageView
        var setting=itemView.setting
        var favourite=itemView.favourite
        var Reflection=itemView.Reflection
        var share=itemView.Share
         */
      var setting=itemView.settingkey
      var favourite=itemView.favouritekey
      var Reflection=itemView.Reflectionkey
      var share=itemView.Sharekey
        var text=itemView.text
    }

    private fun removeItem(position: Int, context: Context) {
        var id=position
        var item: PojoAllQuoteDetail = userlist.get(position)
        Log.d("Data is ","${item.text}")
        var client=ApiCall()
        var retrofit=client.retrofitClient()
        val retrofitAp = retrofit?.create(RedditAPI::class.java)
        var obi=LoginDataUser.instance
        var call = retrofitAp?.GetMark(obi.token!!,item.id)
        call?.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: retrofit2.Response<ResponseBody>?) {
                Log.d("server", "onResponse: Server Response: " + response.toString());

                try {
                    var json:String?=null
                    json= response?.body()?.string()
                    if (json==null) {
                        Log.d("String", "yes")
                    }
                    else {
                        Log.d("fav", "onResponse: json: " + json);
                        var data: JSONObject? = null;
                        data = JSONObject(json);
                        Log.d("Favourite ","${data}")
                        var message = data.getString("message")
                        Log.d("Status ","${message}")
                        if(message.equals("Favorite removed successfully"))
                        {
                            userlist.removeAt(position);
                            notifyItemRemoved(position);
                        }
                        else
                        {
                            Log.d("Error","Error Occured")
                        }
                    }


                } catch (e: JSONException) {
                    Log.e("JSONException", "onResponse: JSONException: ");
                } catch (e: IOException) {
                    Log.e("IOexception", "onResponse: JSONException: ");
                }

            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.e("OnFailure", "onFailure: Something went wrong: ")
                //Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        })
    }
}