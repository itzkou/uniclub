package com.kou.uniclub.Extensions

import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.widget.EditText

interface Validation {

    fun String.isValidEmail(): Boolean
            = this.isNotEmpty() &&
            Patterns.EMAIL_ADDRESS.matcher(this).matches()&&this.length >= 6


    fun String.isValidName():Boolean
            =this.isNotEmpty()&&this.matches(Regex("^[\\p{L} .'-]+$"))&&this.length>=4

    fun String.isValidPhone():Boolean
            =this.isNotEmpty()&&Patterns.PHONE.matcher(this).matches()&&this.length==8

    fun String.isValidPassword():Boolean
            =this.isNotEmpty()&&this.matches(Regex("^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#'&\$%]).{6,20}$"))&&this.length>=6





    fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit){
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