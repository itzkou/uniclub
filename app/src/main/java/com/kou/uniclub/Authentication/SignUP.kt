package com.kou.uniclub.Authentication

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.kou.uniclub.R
import kotlinx.android.synthetic.main.activity_sign_up.*

class SignUP : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        //redirect
        back.setOnClickListener {
            startActivity(Intent(this@SignUP,Auth::class.java))
        }
    }
}
