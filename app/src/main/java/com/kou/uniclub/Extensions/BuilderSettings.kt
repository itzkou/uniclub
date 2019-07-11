package com.kou.uniclub.Extensions

import android.content.Context
import android.content.Intent
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.RadioButton
import android.widget.TextView
import com.facebook.login.LoginManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.jaredrummler.materialspinner.MaterialSpinner
import com.kou.uniclub.Activities.Authentification.SignIN
import com.kou.uniclub.Activities.Home
import com.kou.uniclub.R
import com.kou.uniclub.SharedUtils.PrefsManager


class BuilderSettings {

    companion object {


        fun showSettings(context: Context) {
            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()//request email id
                .build()
            // Build a GoogleSignInClient with the options specified by gso.
            val mGoogleSignInClient = GoogleSignIn.getClient(context, gso)
            val dialogView = LayoutInflater.from(context).inflate(com.kou.uniclub.R.layout.builder_settings, null)
            val logout = dialogView.findViewById<TextView>(com.kou.uniclub.R.id.tvLogout)
            val radioNotifs = dialogView.findViewById<RadioButton>(com.kou.uniclub.R.id.radioNotifs)
            val spReminder=dialogView.findViewById<MaterialSpinner>(R.id.spReminder)
            val tvRdes=dialogView.findViewById<TextView>(R.id.tvRdesc)
            val builder = AlertDialog.Builder(context, com.kou.uniclub.R.style.CustomAlertDialog)
            builder.setView(dialogView)
            val dialog = builder.create()
            dialog.window!!.setGravity(Gravity.TOP)
            dialog.show()

            logout.setOnClickListener {
                dialog.dismiss()
                PrefsManager.seToken(context, null)
                //facebook log out
                LoginManager.getInstance().logOut()
                //google sign out
                mGoogleSignInClient.signOut().addOnSuccessListener {
                }
                //fireBase sign out
                FirebaseAuth.getInstance().signOut()


                context.startActivity(Intent(context, SignIN::class.java))
                val activity = context as Home
                activity.finish()

            }
            var radio = false
            radioNotifs.setOnClickListener {
                if (!radio) {
                    radioNotifs.isChecked = true
                    radio = true
                } else {
                    radioNotifs.isChecked = false
                    radio = false
                }
            }

            spReminder.popupWindow.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.builder_round))
            spReminder.setItems("1h", "2h", "3h","4h")
            spReminder.setOnItemSelectedListener { view, position, id, item ->
                tvRdes.text="You will get notifications before ${item.toString()}"
            }


        }
    }
}