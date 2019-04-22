package com.kou.uniclub.Model.Club

import com.google.gson.annotations.SerializedName

data class Club(
    @SerializedName("Description")
    val description: String,
    @SerializedName("Email")
    val email: String,
    @SerializedName("Facebook")
    val facebook: Any?,
    @SerializedName("Location")
    val location: String,
    @SerializedName("Name")
    val name: String,
    @SerializedName("Photo")
    val photo: Any?,
    @SerializedName("President")
    val president: String,
    @SerializedName("created_at")
    val createdAt: Any?,
    @SerializedName("datetimepicker")
    val datetimepicker: Any?,
    @SerializedName("id")
    val id: Int,
    @SerializedName("universitie_id")
    val universitieId: Int,
    @SerializedName("updated_at")
    val updatedAt: Any?,
    @SerializedName("user_id")
    val userId: Int
)