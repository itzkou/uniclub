package com.kou.uniclub.UI
import android.content.Context
import android.support.v8.renderscript.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.Bitmap
import android.view.View




class ImagePreviewerUtils {
    companion object {
        public fun getBlurredScreenDrawable(context: Context, screen: View): BitmapDrawable {
            val screenshot = takeScreenshot(screen)
            val blurred = blurBitmap(context, screenshot)
            return BitmapDrawable(context.getResources(), blurred)
        }

        private fun takeScreenshot(screen: View): Bitmap {
            screen.isDrawingCacheEnabled = true
            val bitmap = Bitmap.createBitmap(screen.drawingCache)
            screen.isDrawingCacheEnabled = false
            return bitmap
        }

        private fun blurBitmap(context: Context, bitmap: Bitmap): Bitmap {
            val bitmapScale = 0.3f
            val blurRadius = 10f

            val width = Math.round(bitmap.width * bitmapScale)
            val height = Math.round(bitmap.height * bitmapScale)

            val inputBitmap = Bitmap.createScaledBitmap(bitmap, width, height, false)
            val outputBitmap = Bitmap.createBitmap(inputBitmap)

            val rs = RenderScript.create(context)
            val theIntrinsic = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs))
            val tmpIn = Allocation.createFromBitmap(rs, inputBitmap)
            val tmpOut = Allocation.createFromBitmap(rs, outputBitmap)
            theIntrinsic.setRadius(blurRadius)
            theIntrinsic.setInput(tmpIn)
            theIntrinsic.forEach(tmpOut)
            tmpOut.copyTo(outputBitmap)

            return outputBitmap
        }
    }
    }
