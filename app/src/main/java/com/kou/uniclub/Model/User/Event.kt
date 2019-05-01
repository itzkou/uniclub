package com.kou.uniclub.Model.User


import com.google.gson.annotations.SerializedName

data class Event(
    @SerializedName("Animated_By")
    val animatedBy: String,
    @SerializedName("club_id")
    val clubId: Int,
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("datetimepicker")
    val datetimepicker: String,
    @SerializedName("Description")
    val description: String,
    @SerializedName("End_Time")
    val endTime: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("Location")
    val location: String,
    @SerializedName("Name")
    val name: String,
    @SerializedName("Photo")
    val photo: String,
    @SerializedName("Start_Time")
    val startTime: String,
    @SerializedName("updated_at")
    val updatedAt: String
)