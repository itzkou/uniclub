package com.kou.uniclub.Model.University.NoPagination


import com.google.gson.annotations.SerializedName
import com.kou.uniclub.Model.University.University

data class UniversitiesResponse(
    @SerializedName("data")
    val univs: ArrayList<University>,
    @SerializedName("success")
    val success: Boolean
)