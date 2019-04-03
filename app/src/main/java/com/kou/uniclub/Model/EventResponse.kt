package com.kou.uniclub.Model

import com.google.gson.annotations.SerializedName

data class EventResponse(
    @SerializedName("data")
    val `data`: List<Event>,
    @SerializedName("success")
    val success: Boolean
)