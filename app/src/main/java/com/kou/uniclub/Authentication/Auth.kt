package com.kou.uniclub.Authentication


import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle

import com.kou.uniclub.R
import kotlinx.android.synthetic.main.activity_auth.*

class Auth : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)

        btn_signup.setOnClickListener {
            startActivity(Intent(this@Auth,SignUP::class.java))

        }
        btn_signin.setOnClickListener {
            startActivity(Intent(this@Auth,SignIN::class.java))
        }



    }






}
