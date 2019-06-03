package com.kou.uniclub.Model.Event.NoPagination

import com.google.gson.annotations.SerializedName
import com.kou.uniclub.Model.Event.EventX

data class EventDetailsResponse(
    @SerializedName("data")
    val event: EventX,
    @SerializedName("success")
    val success: Boolean
)