package com.developer.rohal.mantra


import android.graphics.Typeface
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.android.synthetic.main.fragment_splash_screen.*
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import android.content.Intent
import android.os.Handler


/**
 * A simple [Fragment] subclass.
 */
class fragmentSplashScreen : Fragment() {

    private val SPLASH_TIME_OUT = 2000
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_splash_screen, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Handler().postDelayed(Runnable
        {
            val transaction = fragmentManager?.beginTransaction()
            transaction?.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right)
            var fragmentLogin = fragmentLogin()
            transaction?.replace(R.id.container, fragmentLogin)
            transaction?.commit()
        }, SPLASH_TIME_OUT.toLong())
    }


}
