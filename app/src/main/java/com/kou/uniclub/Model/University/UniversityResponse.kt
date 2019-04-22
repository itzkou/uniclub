package com.kou.uniclub.Model.University

import com.google.gson.annotations.SerializedName

data class UniversityResponse(
    @SerializedName("data")
    val pagination: Pagination,
    @SerializedName("success")
    val success: Boolean
)