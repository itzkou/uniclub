package com.kou.uniclub.Activities.Authentification

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.RadioButton
import com.kou.uniclub.R
import kotlinx.android.synthetic.main.activity_user_category.*

class UserCategory : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_category)

        /*  myClicks(rdStudent, rdPro)
          myClicks(rdPro, rdStudent)*/


    }

    fun myClicks(rd: RadioButton, rdi: RadioButton) {
        var c = false
        rd.setOnClickListener {

            c = if (!c) {
                rd.setButtonDrawable(R.drawable.tick_orange)
                rdi.isClickable = false
                btnConfirm.setOnClickListener {
                    when (rd.id) {
                        R.id.rdStudent -> {
                            startActivity(Intent(this@UserCategory, StudentSignUp::class.java))
                            finish()
                        }
                        R.id.rdPro -> {
                            startActivity(Intent(this@UserCategory, ProSignUp::class.java))
                            finish()
                        }

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

    fun onRadioButtonClicked(view: View) {
        if (view is RadioButton) {

            val checked = view.isChecked

            btnConfirm.setOnClickListener {
                when (view.getId()) {
                    R.id.rdStudent -> {
                      if (checked)
                            startActivity(Intent(this@UserCategory, StudentSignUp::class.java))

                    }
                    R.id.rdPro ->
                        if (checked) {
                            startActivity(Intent(this@UserCategory, ProSignUp::class.java))
                        }
                }
            }

            when (view.getId()) {
                R.id.rdStudent -> {rdStudent.setButtonDrawable(R.drawable.tick_orange)
                rdPro.setButtonDrawable(R.drawable.ellipse)}
                R.id.rdPro ->{rdPro.setButtonDrawable(R.drawable.tick_orange)
                    rdStudent.setButtonDrawable(R.drawable.ellipse)}

            }

        }
    }
}
