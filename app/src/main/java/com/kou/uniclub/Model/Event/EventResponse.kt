package com.kou.uniclub.Model.Event

import com.google.gson.annotations.SerializedName

data class EventResponse(
    @SerializedName("data")
    val event: EventX,
    @SerializedName("success")
    val success: Boolean
)