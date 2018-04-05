package com.developer.rohal.mantra


import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.renderscript.Allocation
import android.renderscript.ScriptIntrinsicBlur
import android.renderscript.RenderScript
import android.graphics.Bitmap
import android.renderscript.Element
import kotlinx.android.synthetic.main.fragment_account_setting.*
import android.graphics.BitmapFactory
import android.net.Uri
import android.view.WindowManager
import java.net.URL
import android.graphics.drawable.Drawable
import android.support.design.widget.Snackbar
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import com.developer.rohal.mantra.R.styleable.SimpleDraweeView
import java.io.InputStream
import java.lang.System.load
import jp.wasabeef.fresco.processors.BlurPostprocessor
import com.facebook.imagepipeline.request.Postprocessor
import com.facebook.imagepipeline.request.ImageRequestBuilder
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.drawee.backends.pipeline.PipelineDraweeController








/**
 * A simple [Fragment] subclass.
 */
class fragmentAccountSetting : Fragment() {

    private val BITMAP_SCALE = 0.6f
    private val BLUR_RADIUS = 15f

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val bitmap = BitmapFactory.decodeResource(resources, R.drawable.dp)
        //val blurredBitmap = blur(context,bitmap)
        //Imageblur.setImageBitmap(blurredBitmap)
        account.setImageURI(Uri.parse("http://www.baltana.com/files/wallpapers-3/Undertaker-HD-Wallpapers-09435.png"),context)
        val postprocessor = BlurPostprocessor(context, 30)
        val imageRequest = ImageRequestBuilder.newBuilderWithSource(Uri.parse("http://www.baltana.com/files/wallpapers-3/Undertaker-HD-Wallpapers-09435.png"))
                .setPostprocessor(postprocessor)
                .build()
        var controller = Fresco.newDraweeControllerBuilder()
                .setImageRequest(imageRequest)
                .setOldController(Imageblur.getController())
                .build() as PipelineDraweeController
        Imageblur.setController(controller);
        AccountSettingBackButton.setOnClickListener {
            activity?.onBackPressed()
        }
       Update.setOnClickListener {
            var snackbar1: Snackbar = Snackbar.make(it, "Updated your Account", Snackbar.LENGTH_SHORT);
            snackbar1.show()
            activity?.onBackPressed()
        }
        var myRotation: Animation = AnimationUtils.loadAnimation(context, R.anim.rotation);
        PicClick.startAnimation(myRotation)
        this.activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
    }

    fun blur(context: Context?, image: Bitmap): Bitmap {
        val width = Math.round(image.width * BITMAP_SCALE)
        val height = Math.round(image.height * BITMAP_SCALE)
        val inputBitmap = Bitmap.createScaledBitmap(image, width, height, false)
        val outputBitmap = Bitmap.createBitmap(inputBitmap)
        val rs = RenderScript.create(context)
        val intrinsicBlur = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs))
        val tmpIn = Allocation.createFromBitmap(rs, inputBitmap)
        val tmpOut = Allocation.createFromBitmap(rs, outputBitmap)
        intrinsicBlur.setRadius(BLUR_RADIUS)
        intrinsicBlur.setInput(tmpIn)
        intrinsicBlur.forEach(tmpOut)
        tmpOut.copyTo(outputBitmap)
        return outputBitmap
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        return inflater!!.inflate(R.layout.fragment_account_setting, container, false)
    }




}
