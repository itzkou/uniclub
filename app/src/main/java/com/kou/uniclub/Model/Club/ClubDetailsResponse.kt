package com.kou.uniclub.Model.Club


import com.google.gson.annotations.SerializedName

data class ClubDetailsResponse(
    @SerializedName("data")
    val club: ClubX,
    @SerializedName("success")
    val success: Boolean
)