package com.example.disaster.model

data class FloodRequest(
    val id: Int,
    val user_id: String,
    val name: String,
    val phone: String,
    val location: String,
    val status: String,
    var image:String,
    val created_at: String,
    val isVerified: String,
    var supporterid: String,
    var supportername: String,
    var supporterphone: String,
)
