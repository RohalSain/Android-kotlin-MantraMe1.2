package com.developer.rohal.mantra

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.provider.MediaStore
import android.util.Log
import kotlinx.android.synthetic.main.camera_gallery_box.*
import kotlinx.android.synthetic.main.dialog_box.view.*

class CameraAndGallery {
    var CustomLoader: Dialog? = null
    var loaderContext: Context? = null
    fun ShowCameraAndGallery(context: Context) {
        loaderContext = context
        CustomLoader = Dialog(context)
        CustomLoader!!.setContentView(R.layout.camera_gallery_box)
        CustomLoader!!.getWindow().setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        CustomLoader!!.window.attributes.windowAnimations = R.style.customAnimationsgrow
        CustomLoader!!.show()
        CustomLoader!!.btn_Camera.setOnClickListener {
            //builder.setInverseBackgroundForced(true)
            //Dilog.dismiss()
        }
        CustomLoader!!.btn_Camera.setOnClickListener {
            //dispatchTakePictureIntent()
            //Dilog.dismiss()
        }
    }

}
