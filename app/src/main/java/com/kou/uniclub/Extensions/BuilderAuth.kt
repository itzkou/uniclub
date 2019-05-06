package com.kou.uniclub.Extensions

import android.content.Intent
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import com.airbnb.lottie.LottieAnimationView
import com.kou.uniclub.Activities.Authentification.SignUP
import com.kou.uniclub.R


class BuilderAuth{
    companion object {
        fun showDialog(activity: AppCompatActivity){

            val dialogView = LayoutInflater.from(activity).inflate(R.layout.builder_feature_access, null)

            val anim = dialogView.findViewById<LottieAnimationView>(R.id.animAuth)
            val builder = AlertDialog.Builder(activity, R.style.CustomAlertDialog)

            builder.setView(dialogView)

            anim.playAnimation()
            builder.setPositiveButton("confirm") { dialog, which ->
               activity.startActivity(Intent(activity, SignUP::class.java))
            }

            builder.setNegativeButton(
                "Cancel"
            ) { dialog, which ->
                dialog?.dismiss()
            }
            val dialog = builder.create()

            dialog.show()


        }
    }
}