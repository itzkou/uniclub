package com.kou.uniclub.UI

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import com.kou.uniclub.R


class ImagePreviewer {
    fun show(context: Context, source: ImageView) {
        val background = ImagePreviewerUtils.getBlurredScreenDrawable(context, source.rootView)

        val dialogView = LayoutInflater.from(context).inflate(R.layout.view_image_previewer, null)
        val imageView = dialogView.findViewById(R.id.previewer_image) as ImageView

        val copy = source.drawable.constantState!!.newDrawable()
        imageView.setImageDrawable(copy)

        val dialog = Dialog(context, R.style.ImagePreviewerTheme)
        dialog.window!!.setBackgroundDrawable(background)
        dialog.setContentView(dialogView)
        dialog.show()
         source.setOnTouchListener(object: View.OnTouchListener{
             override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                if(dialog.isShowing)
                {
                    v!!.parent.requestDisallowInterceptTouchEvent(true)
                    val action=event!!.actionMasked
                    if(action==MotionEvent.ACTION_UP||action==MotionEvent.ACTION_CANCEL)
                    {
                        v.parent.requestDisallowInterceptTouchEvent(false)
                        dialog.dismiss()
                        return true
                    }
                }
                 return false

             }

         })

    }
}