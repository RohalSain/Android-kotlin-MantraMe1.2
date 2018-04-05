package com.developer.rohal.mantra


import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_feedback.*
import kotlinx.android.synthetic.main.fragment_side_menu_settings.*


/**
 * A simple [Fragment] subclass.
 */
class fragmentFeedback : Fragment() {

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        feedbackBackButton.setOnClickListener {
            activity?.onBackPressed()
        }
        Submit.setOnClickListener {

            if(addReflection.text.isEmpty())
            {
                var snackbar1: Snackbar = Snackbar.make(it, "Empty Feedback", Snackbar.LENGTH_SHORT);
                snackbar1.show()
            }
            else
            {
                var snackbar1: Snackbar = Snackbar.make(it, "Successfully Submitted", Snackbar.LENGTH_SHORT);
                snackbar1.show()
                activity?.onBackPressed()
            }

        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_feedback, container, false)
    }

}// Required empty public constructor
