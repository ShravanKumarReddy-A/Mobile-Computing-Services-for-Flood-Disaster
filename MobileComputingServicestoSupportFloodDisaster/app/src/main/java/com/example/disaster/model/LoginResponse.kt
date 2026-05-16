package com.example.disaster.model


data class LoginResponse(
    val error: Boolean,
    val message: String,
    var data: ArrayList<User>
)