package com.kou.uniclub.Model.Club.Pagination


import com.google.gson.annotations.SerializedName

data class ClubsResponse(
    @SerializedName("data")
    val pagination: Pagination,
    @SerializedName("success")
    val success: Boolean
)