package com.kou.uniclub.Model.Auth

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("access_token")
    val accessToken: String,

    @SerializedName("expires_at")
    val expiresAt: String,

    @SerializedName("token_type")
    val tokenType: String,

    @SerializedName("message")
    val message:String

)