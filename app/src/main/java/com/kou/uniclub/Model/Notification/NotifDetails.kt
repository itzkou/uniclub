package com.kou.uniclub.Model.Notification


import com.google.gson.annotations.SerializedName

data class NotifDetails(
    @SerializedName("Club_ID")
    val clubID: Int,
    @SerializedName("event_id")
    val eventId: Int,
    @SerializedName("event_name")
    val eventName: String,
    @SerializedName("Location")
    val location: String
)