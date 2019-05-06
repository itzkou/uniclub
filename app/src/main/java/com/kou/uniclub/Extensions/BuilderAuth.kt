package com.kou.uniclub.Extensions

import android.content.Context
import android.content.Intent
import android.content.res.AssetManager
import android.graphics.Typeface
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import com.airbnb.lottie.LottieAnimationView
import com.kou.uniclub.Activities.Authentification.SignUP
import com.kou.uniclub.R


class BuilderAuth{
    companion object {
        fun showDialog(context: Context){

            val dialogView = LayoutInflater.from(context).inflate(R.layout.builder_feature_access, null)

            val anim = dialogView.findViewById<LottieAnimationView>(R.id.animAuth)
            val builder = AlertDialog.Builder(context, R.style.CustomAlertDialog)

            builder.setView(dialogView)

            anim.playAnimation()
            builder.setPositiveButton("confirm") { dialog, which ->
               context.startActivity(Intent(context, SignUP::class.java))
            }


            builder.setNegativeButton(
                "Cancel"
            ) { dialog, which ->
                dialog?.dismiss()
            }
            val dialog = builder.create()



            dialog.show()
            val positiveBtn = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
            val negativeBtn = dialog.getButton(AlertDialog.BUTTON_NEGATIVE)

            //positiveBtn.setTextAppearance(context,R.style.ralewayTextview)
            //negativeBtn.setTextAppearance(context,R.style.ralewayTextview)



        }
    }
}