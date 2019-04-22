package com.kou.uniclub.Model.Auth

import com.google.gson.annotations.SerializedName

data class SignUpResponse(
    @SerializedName("message")
    val message: String
)