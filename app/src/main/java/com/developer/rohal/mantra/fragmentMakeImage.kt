package com.developer.rohal.mantra


import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.support.v4.app.Fragment
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_make_image.*
import kotlinx.android.synthetic.main.items.view.*
import android.os.Environment.getDataDirectory
import android.system.Os.accept
import android.util.Log
import com.developer.rohal.mantra.R.id.name
import java.io.File
import java.io.FilenameFilter
import android.provider.MediaStore
import kotlinx.android.synthetic.main.frgament_recycler_view_reflection.*


/**
 * A simple [Fragment] subclass.
 */
class fragmentMakeImage : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_make_image, container, false)
    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        //text.setText(Html.fromHtml("Accept your past without regret. <br> Handle your present with confidence. <br> Face your future without fear.").toString())
        var obj=LoginDataUser.instance
        Log.d("Display","Quote: ${obj.Quotes} Background: ${obj.BackGround}")
        MakeImageText.setText(Html.fromHtml("${obj.Quotes}"))
        makeImageCircular.setImageURI(Uri.parse("http://139.59.18.239:6010/mantrame/${obj.BackGround}"))


    }
}