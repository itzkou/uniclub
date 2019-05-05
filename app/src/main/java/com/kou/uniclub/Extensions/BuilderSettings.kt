package com.kou.uniclub.Extensions

import android.content.Context
import android.support.v7.app.AlertDialog
import android.view.Gravity
import android.view.LayoutInflater
import com.kou.uniclub.R

class BuilderSettings {

    companion object {
        fun showSettings(context: Context){
            val dialogView = LayoutInflater.from(context).inflate(R.layout.builder_settings, null)
            val builder = AlertDialog.Builder(context, R.style.CustomAlertDialog)
            builder.setView(dialogView)
            val dialog = builder.create()

            dialog.window!!.setGravity(Gravity.TOP)
           dialog.show()
        }
    }
}