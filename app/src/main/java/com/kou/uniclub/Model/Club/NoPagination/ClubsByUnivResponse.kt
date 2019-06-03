package com.kou.uniclub.Model.Club.NoPagination

import com.google.gson.annotations.SerializedName
import com.kou.uniclub.Model.Club.ClubX

data class ClubsByUnivResponse(
    @SerializedName("data")
    val clubs: ArrayList<ClubX>,
    @SerializedName("success")
    val success: Boolean
)