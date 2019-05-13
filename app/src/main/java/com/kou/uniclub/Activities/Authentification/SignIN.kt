package com.kou.uniclub.Activities.Authentification

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import com.kou.uniclub.Activities.Home
import com.kou.uniclub.Extensions.Validation
import com.kou.uniclub.Model.Auth.LoginResponse
import com.kou.uniclub.Network.UniclubApi
import com.kou.uniclub.R
import com.kou.uniclub.SharedUtils.PrefsManager
import kotlinx.android.synthetic.main.activity_sign_in.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignIN : AppCompatActivity(),Validation {
    //private lateinit var mail: String
    //private lateinit var password: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)
       btnFb.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)

        btnSignin.setOnClickListener {
            uniSignIn(edEmail.text.toString(),edPassword.text.toString())
        }

        tvSignUp.setOnClickListener {
            startActivity(Intent(this@SignIN,SignUP::class.java))
        }

    }
    private fun uniSignIn(m:String,p:String) {
        val service=UniclubApi.create()
        service.signIN(m, p).enqueue(object : Callback<LoginResponse> {
            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Toast.makeText(this@SignIN,t.message,Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful) {
                    PrefsManager.seToken(this@SignIN, response.body()!!.accessToken)
                    startActivity(Intent(this@SignIN, Home::class.java))
                }

                else if (response.code()==404)
                    Toast.makeText(this@SignIN,"Wrong email or password!",Toast.LENGTH_SHORT).show()


            }

        })
    }


}
