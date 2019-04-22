package com.kou.uniclub.Model.User

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("Adress")
    val adress: String,
    @SerializedName("Birth_Date")
    val birthDate: String,
    @SerializedName("Email")
    val email: String,
    @SerializedName("First_Name")
    val firstName: String,
    @SerializedName("Gender")
    val gender: String,
    @SerializedName("Last_Name")
    val lastName: String,
    @SerializedName("Personal_Description")
    val personalDescription: Any?,
    @SerializedName("Phone_Number")
    val phoneNumber: Any?,
    @SerializedName("active")
    val active: Int,
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("deleted_at")
    val deletedAt: Any?,
    @SerializedName("id")
    val id: Int,
    @SerializedName("image")
    val image: String,
    @SerializedName("role_id")
    val roleId: Int,
    @SerializedName("status")
    val status: Any?,
    @SerializedName("updated_at")
    val updatedAt: String
)