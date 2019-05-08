package com.kou.uniclub.Extensions

import android.content.Context
import android.content.Intent
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import com.airbnb.lottie.LottieAnimationView
import com.kou.uniclub.Activities.Authentification.SignUP
import com.kou.uniclub.R

class BuilderSearchFilter {
    companion object {
        fun showDialog(context: Context) {

            val dialogView = LayoutInflater.from(context).inflate(com.kou.uniclub.R.layout.builder_search_filter, null)

            val builder = AlertDialog.Builder(context, R.style.CustomAlertDialog)

            builder.setView(dialogView)

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


        }
    }
}