package com.kou.uniclub.Model.User


import com.google.gson.annotations.SerializedName

data class MyfavoritesResponse(
    @SerializedName("data")
    val `data`: List<Event>,
    @SerializedName("success")
    val success: Boolean
)