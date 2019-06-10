package com.kou.uniclub.Model.Notification


import com.google.gson.annotations.SerializedName

data class NotificationResponse(
    @SerializedName("data")
    val notifs: ArrayList<Notification>,
    @SerializedName("success")
    val success: Boolean
)