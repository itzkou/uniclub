package com.kou.uniclub.Model.User


import com.google.gson.annotations.SerializedName
import com.kou.uniclub.Model.Event.EventX

data class MyfavoritesResponse(
    @SerializedName("data")
    val events: ArrayList<EventX>,

    @SerializedName("success")
    val success: Boolean,

    @SerializedName("message")
    val  message:String
)