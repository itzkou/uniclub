package com.kou.uniclub.Model

import com.google.gson.annotations.SerializedName

data class Event(
    @SerializedName("club_id")
    val clubId: Int,
    @SerializedName("created_at")
    val createdAt: Any?,
    @SerializedName("date")
    val date: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("libele")
    val libele: String,
    @SerializedName("lieu")
    val lieu: String,
    @SerializedName("photo")
    val photo: String,
    @SerializedName("updated_at")
    val updatedAt: Any?
)