package com.kou.uniclub.Model.Event

import com.google.gson.annotations.SerializedName

data class EventListResponse(
    @SerializedName("data")
    val pagination: Pagination,
    @SerializedName("success")
    val success: Boolean
)