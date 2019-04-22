package com.kou.uniclub.Activities.Authentification

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.kou.uniclub.R
import kotlinx.android.synthetic.main.activity_sign_in.*

class SignIN : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        back.setOnClickListener {
            //startActivity(Intent(this@SignIN,Auth::class.java))
        }
    }
}
