package com.developer.rohal.mantra


import android.content.Context
import android.graphics.Typeface
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import kotlinx.android.synthetic.main.fragment_forgort_password_verify.*
import kotlinx.android.synthetic.main.fragment_forgot_password.*
import kotlinx.android.synthetic.main.fragment_login.*


/**
 * A simple [Fragment] subclass.
 */
class fragmentForgortPasswordVerify : Fragment() {

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        var titleFont = Typeface.createFromAsset(context?.assets, "fonts/Billy Ohio.ttf")
        forgotVerfiyApp.setTypeface(titleFont)
        forgotVerfiyBackButton.setOnClickListener {
            activity?.onBackPressed()
        }
        this.activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
        btnResetPassword.setOnClickListener {
            var status: Boolean = true
            if (txtVerifyCode.text.trim().isEmpty()) {
                status = false
                //Toast.makeText(context, "Invalid Email", Toast.LENGTH_SHORT).show()
                var snackbar1: Snackbar = Snackbar.make(it, "Enter Code!", Snackbar.LENGTH_SHORT);
                snackbar1.show();
            }
            else if(txtForgotVerifyNewPassword.text.trim().isEmpty())
            {
                status = false
                //Toast.makeText(context, "Invalid Password", Toast.LENGTH_SHORT).show()
                var snackbar1: Snackbar = Snackbar.make(it, "Enter New Password!", Snackbar.LENGTH_SHORT);
                snackbar1.show();
            }
            else if(status==true) {
                val transaction = fragmentManager?.beginTransaction()
                transaction?.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right)
                var fragmentLogin = fragmentDashboard()
                transaction?.replace(R.id.container, fragmentLogin)
                transaction?.addToBackStack("Dashboard Page")
                transaction?.commit()
            }

        }
        taskForgotVerfiy.setOnClickListener {
            it.hideKeyboard()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_forgort_password_verify, container, false)
    }
    fun View.hideKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }
}
