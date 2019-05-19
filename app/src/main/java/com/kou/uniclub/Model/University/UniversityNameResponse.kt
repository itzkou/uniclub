package com.kou.uniclub.Model.University


import com.google.gson.annotations.SerializedName

data class UniversityNameResponse(
    @SerializedName("data")
    val university: ArrayList<University>,
    @SerializedName("success")
    val success: Boolean
)