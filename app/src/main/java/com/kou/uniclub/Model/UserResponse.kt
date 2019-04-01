package com.kou.uniclub.Model

import com.google.gson.annotations.SerializedName

data class UserResponse(
    @SerializedName("user")
    val user: User
)