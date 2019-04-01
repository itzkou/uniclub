package com.kou.uniclub.Model

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("email")
    val email: String,
    @SerializedName("password")
    val password: String,

    @SerializedName("name")
    val name: String,

    @SerializedName("status")
    val status: Int,

    @SerializedName("category_id")
    val categoryId: Int,


    @SerializedName("image")
    val image: String


)