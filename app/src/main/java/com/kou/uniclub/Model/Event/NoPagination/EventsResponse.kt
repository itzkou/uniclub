package com.kou.uniclub.Model.Event.NoPagination


import com.google.gson.annotations.SerializedName
import com.kou.uniclub.Model.Event.EventX

data class EventsResponse(
    @SerializedName("data")
    val events: ArrayList<EventX>,
    @SerializedName("success")
    val success: Boolean
)