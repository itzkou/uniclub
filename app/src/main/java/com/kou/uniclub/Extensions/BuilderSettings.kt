package com.kou.uniclub.Extensions

import android.content.Context
import android.content.Intent
import android.support.v7.app.AlertDialog
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.RadioButton
import android.widget.TextView
import com.kou.uniclub.Activities.Authentification.StudentSignUP
import com.kou.uniclub.R
import com.kou.uniclub.SharedUtils.PrefsManager

class BuilderSettings {

    companion object {
        fun showSettings(context: Context){
            val dialogView = LayoutInflater.from(context).inflate(R.layout.builder_settings, null)
            val logout=dialogView.findViewById<TextView>(R.id.tvLogout)
            val radioNotifs=dialogView.findViewById<RadioButton>(R.id.radioNotifs)
            val builder = AlertDialog.Builder(context, R.style.CustomAlertDialog)
            builder.setView(dialogView)
            val dialog = builder.create()

            dialog.window!!.setGravity(Gravity.TOP)
           dialog.show()

            logout.setOnClickListener {
                PrefsManager.seToken(context,null)
                context.startActivity(Intent(context,StudentSignUP::class.java))

            }
            var radio=false
            radioNotifs.setOnClickListener {
                if(!radio) {
                    radioNotifs.isChecked = true
                    radio=true
                }
                else{
                    radioNotifs.isChecked=false
                    radio=false
                }
            }
        }
    }
}