package com.kou.uniclub.Model.User.Behaviour


import com.google.gson.annotations.SerializedName

data class FollowResponse(
    @SerializedName("message")
    val message: String
)