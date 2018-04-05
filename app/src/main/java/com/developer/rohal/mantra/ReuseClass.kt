package com.developer.rohal.mantra

public class ReuseClass private constructor() {
    init {
        println("This ($this) is a ReuseClass")
    }

    private object Holder {
        val INSTANCE = ReuseClass()
    }

    companion object {
        val instance: ReuseClass by lazy { Holder.INSTANCE }
    }

    var Quote: String? = null
    var BackGround:String?= null
}