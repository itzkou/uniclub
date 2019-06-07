package com.kou.uniclub.Extensions

import android.support.design.widget.TextInputEditText
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns

interface Validation {

    fun String.isValidEmail(): Boolean
            = this.isNotEmpty() &&
            Patterns.EMAIL_ADDRESS.matcher(this).matches()&&this.length >= 2


    fun String.isValidName():Boolean
            =this.isNotEmpty()&&this.matches(Regex("^[\\p{L} .'-]+$"))&&this.length>=4

    fun String.isValidPhone():Boolean
            =this.isNotEmpty()&&Patterns.PHONE.matcher(this).matches()&&this.length==8

    fun String.isValidPassword():Boolean
            =this.isNotEmpty()&&this.matches(Regex("^(?=.*\\d).{6,15}\$"))





    fun TextInputEditText.afterTextChanged(afterTextChanged: (String) -> Unit){
        this.addTextChangedListener(object: TextWatcher {

            override fun afterTextChanged(s: Editable?){
                afterTextChanged.invoke(s.toString())



            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {


            }
        })

    }




}