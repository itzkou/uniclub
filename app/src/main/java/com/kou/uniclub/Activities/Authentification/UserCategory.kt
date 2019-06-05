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
