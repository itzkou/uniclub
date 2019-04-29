package com.kou.uniclub.Model.Auth

import com.google.gson.annotations.SerializedName

data class Data(
    @SerializedName("First_Name")
    val firstName: List<String>,

    @SerializedName("Last_Name")
    val lastName: List<String>,

    @SerializedName("Birth_Date")
    val birthDate: List<String>,

    @SerializedName("Gender")
    val gender: List<String>,

    @SerializedName("Email")
    val email: List<String>,

    @SerializedName("password")
    val password: List<String>,

    @SerializedName("password_confirmation")
    val passwordC: List<String>,

    @SerializedName("Adress")
    val adress: List<String>








)