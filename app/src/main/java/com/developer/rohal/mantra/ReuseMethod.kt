package com.developer.rohal.mantra

import android.support.design.widget.Snackbar
import android.view.View

class ReuseMethod {
    fun showSnakBar(view: View?,message:String)
    {
        var snackbar: Snackbar = Snackbar.make(view, "${message}", Snackbar.LENGTH_SHORT);
        snackbar.show();
    }
}