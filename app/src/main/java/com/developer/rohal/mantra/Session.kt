package com.developer.rohal.mantra

import android.content.Context
import android.content.SharedPreferences

class Session(internal var ctx: Context) {
    internal var prefs: SharedPreferences
    internal var editor: SharedPreferences.Editor

    init {
        prefs = ctx.getSharedPreferences("appdata", Context.MODE_PRIVATE)
        editor = prefs.edit()
    }

    fun setLoggedin(logggedin: Boolean,emial:String,pass:String,token:String,profilePic:String,name:String) {
        editor.putBoolean("loggedInmode", logggedin)
        editor.putString("email", emial)
        editor.putString("pass", pass)
        editor.putString("token",token)
        editor.putString("profilePic",profilePic)
        editor.putString("name",name)
        editor.commit()
    }
    fun loggedin(): Boolean {
        return prefs.getBoolean("loggedInmode", false)
    }
    fun getToken(): String {
        return prefs.getString("token","empty")
    }
    fun getPrfofilePic(): String {
        return prefs.getString("profilePic","empty")
    }
    fun getName(): String {
        return prefs.getString("name","empty")
    }
    fun removeAll()
    {
        prefs.edit().remove("appdata").commit()
    }
    fun getData():String
    {
        var login=prefs.getBoolean("loggedInmode", false)
        var emial=prefs.getString("email","empty")
        var pass=prefs.getString("pass", "empty")
        var token=prefs.getString("token", "empty")
        return  "login ${login} emial ${emial} pass ${pass} token ${token}"
    }
    fun setLoginStatus() {
        editor.putBoolean("loggedInmode",false)
        editor.commit()
    }
}