package com.kou.uniclub.Model

import com.google.gson.annotations.SerializedName

data class Token(
    @SerializedName("token")
    val token: String
)