package com.kou.uniclub.Extensions

import android.content.Context
import android.content.Intent
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import com.airbnb.lottie.LottieAnimationView
import com.kou.uniclub.Activities.Authentification.StudentSignUP
import com.kou.uniclub.R


class BuilderAuth {
    companion object {
        fun showDialog(context: Context) {

            val dialogView = LayoutInflater.from(context).inflate(com.kou.uniclub.R.layout.builder_feature_access, null)

            val anim = dialogView.findViewById<LottieAnimationView>(com.kou.uniclub.R.id.animAuth)
            val builder = AlertDialog.Builder(context, R.style.CustomAlertDialog)

            builder.setView(dialogView)

            anim.playAnimation()
            builder.setPositiveButton("confirm") { dialog, which ->
                context.startActivity(Intent(context, StudentSignUP::class.java))
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