package com.kou.uniclub.Model.Event

import com.google.gson.annotations.SerializedName

data class PassedResponse (
    @SerializedName("data")
    val events: List<EventX>,
    @SerializedName("success")
    val success: Boolean
)