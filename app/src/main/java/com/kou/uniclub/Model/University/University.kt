package com.kou.uniclub.Model.University

import com.google.gson.annotations.SerializedName

data class University(
    @SerializedName("Email")
    val email: String,
    @SerializedName("Location")
    val location: String,
    @SerializedName("Name")
    val name: String,
    @SerializedName("Photo")
    val photo: String,
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("updated_at")
    val updatedAt: String
)