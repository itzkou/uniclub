package com.kou.uniclub.Model.Notification


import com.google.gson.annotations.SerializedName

data class Notif(
    @SerializedName("data")
    val details: NotifDetails,
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("id")
    val id: String,
    @SerializedName("notifiable_id")
    val notifiableId: Int,
    @SerializedName("notifiable_type")
    val notifiableType: String,
    @SerializedName("read_at")
    val readAt: Any?,
    @SerializedName("type")
    val type: String,
    @SerializedName("updated_at")
    val updatedAt: String,
    var selected:Boolean=false
)