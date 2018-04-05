package com.developer.rohal.mantra

import android.content.Context
import android.graphics.Bitmap

public class LoginDataUser private constructor() {
    init {
        println("This ($this) is a singleton")
    }

    private object Holder {
        val INSTANCE = LoginDataUser()
    }

    companion object {
        val instance:LoginDataUser by lazy { Holder.INSTANCE }
    }

    var token: String? = null
    var Quotes: String? = null
    var BackGround:String?= null
    var count:Int=0;
    var QuoteID:String=" "
    var profiePic:String=" "
    var context:Context?=null
    var FacebookGooglebitmap:Bitmap?=null
    var countUpdate:Int=0;
}