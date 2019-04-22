package com.kou.uniclub.Model.Event

import com.google.gson.annotations.SerializedName

data class UpcomingResponse(
    @SerializedName("data")
    val events: List<EventX>,
    @SerializedName("success")
    val success: Boolean
)