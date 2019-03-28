package com.kou.uniclub.Model

data class Club(
    val created_at: String,
    val email: String,
    val id: Int,
    val lieu: String,
    val nom: String,
    val photo: String,
    val universite_id: Int,
    val updated_at: Any,
    val user_id: Int)