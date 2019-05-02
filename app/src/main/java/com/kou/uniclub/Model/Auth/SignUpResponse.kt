package com.kou.uniclub.Model.Auth


import com.google.gson.annotations.SerializedName

data class SignUpResponse(
    @SerializedName("data")
    val `data`: Data,
    @SerializedName("message")
    val message: String,
    @SerializedName("success")
    val success: Boolean
)