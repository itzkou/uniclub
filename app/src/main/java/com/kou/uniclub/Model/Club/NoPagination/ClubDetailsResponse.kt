package com.kou.uniclub.Model.Club.NoPagination


import com.google.gson.annotations.SerializedName
import com.kou.uniclub.Model.Club.ClubX

data class ClubDetailsResponse(
    @SerializedName("data")
    val club: ClubX,
    @SerializedName("success")
    val success: Boolean
)