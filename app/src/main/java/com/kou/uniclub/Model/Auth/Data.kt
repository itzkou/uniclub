package com.kou.uniclub.Model.Auth

import com.google.gson.annotations.SerializedName

data class Data(
    @SerializedName("Birth_Date")
    val birthDate: List<String>
)