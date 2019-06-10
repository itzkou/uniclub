package com.kou.uniclub.Model.Notification


import com.google.gson.annotations.SerializedName

data class NotificationResponse(
    @SerializedName("data")
    val notifs: ArrayList<Notif>,
    @SerializedName("success")
    val success: Boolean
)