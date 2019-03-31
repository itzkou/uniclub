package com.kou.uniclub.Authentication

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.kou.uniclub.Extensions.Validation
import com.kou.uniclub.R
import kotlinx.android.synthetic.main.activity_sign_up.*

class SignUP : AppCompatActivity(),Validation {


     private var cities = arrayOf("","Ariana", "Tunis", "Bizerte")
    private var city:String?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        //redirect
        back.setOnClickListener {
            startActivity(Intent(this@SignUP,Auth::class.java))
        }
    }

    override fun onStart() {
        super.onStart()
        ed_username.afterTextChanged {
            ed_username.error=if( it.isValidName() && it.length>=4) null else "invalid username"
        }
        ed_email.afterTextChanged {
            ed_email.error=if (it.length >= 6 &&it.isValidEmail())  null
            else "invalid email"
        }

        ed_mobile.afterTextChanged {
            ed_mobile.error=if(it.length==8&&it.isValidPhone()) null
            else "enter an 8-digit phone number"
        }

        sp_region.adapter=ArrayAdapter(this@SignUP,android.R.layout.simple_spinner_dropdown_item,cities)
        sp_region.onItemSelectedListener=object:AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                city=cities[position]
            }

        }



        btn_signup.setOnClickListener {


        }

    }
}
