package com.developer.rohal.mantra

import android.os.Bundle
import android.support.multidex.MultiDex
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.facebook.drawee.backends.pipeline.Fresco
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Fresco.initialize(this)
        setContentView(R.layout.activity_main)
        MultiDex.install(this);
        var session = Session(applicationContext)
        Log.d("Data", "${session.getData()}")

        if (session.loggedin() == false) {
            var fragmentLogin = fragmentSplashScreen()
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.container, fragmentLogin)
            transaction.commit()
        } else if (session.loggedin() == true) {
            var ob = LoginDataUser.instance
            ob.token = session.getToken()
            var fragmentLogin = fragmentDashboard()
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.container, fragmentLogin)
            transaction.commit()
        }

    }

    override fun onBackPressed() {
        super.onBackPressed()
    }
}

