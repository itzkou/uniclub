package com.kou.uniclub.Model.Club


import com.google.gson.annotations.SerializedName

data class Pagination(
    @SerializedName("data")
    val clubs: ArrayList<ClubX>,
    @SerializedName("current_page")
    val currentPage: Int,
    @SerializedName("first_page_url")
    val firstPageUrl: String,
    @SerializedName("from")
    val from: Int,
    @SerializedName("last_page")
    val lastPage: Int,
    @SerializedName("last_page_url")
    val lastPageUrl: String,
    @SerializedName("next_page_url")
    val nextPageUrl: String,
    @SerializedName("path")
    val path: String,
    @SerializedName("per_page")
    val perPage: Int,
    @SerializedName("prev_page_url")
    val prevPageUrl: Any?,
    @SerializedName("to")
    val to: Int,
    @SerializedName("total")
    val total: Int
)