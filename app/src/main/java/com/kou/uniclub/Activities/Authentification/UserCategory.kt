package com.kou.uniclub.Activities.Authentification

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.RadioButton
import android.widget.Toast
import com.kou.uniclub.R
import kotlinx.android.synthetic.main.activity_user_category.*

class UserCategory : AppCompatActivity() {
    private var category = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_category)

        onRadioButtonClicked(rdStudent, rdPro)
        onRadioButtonClicked(rdPro, rdStudent)


    }

    fun onRadioButtonClicked(rd: RadioButton, rdi: RadioButton) {
        var c = false
        rd.setOnClickListener {

            c = if (!c) {
                rd.setButtonDrawable(R.drawable.tick_orange)
                rdi.isClickable = false
                btnConfirm.setOnClickListener {
                    when (rd.id) {
                        R.id.rdStudent -> startActivity(Intent(this@UserCategory, StudentSignUp::class.java))
                        R.id.rdPro -> startActivity(Intent(this@UserCategory, ProSignUp::class.java))

                    }

                }
                true

            } else {
                rd.setButtonDrawable(R.drawable.ellipse)
                rdi.isClickable = true
                false

            }


        }


    }
}
