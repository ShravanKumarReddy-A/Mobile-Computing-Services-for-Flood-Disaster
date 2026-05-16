package com.example.disaster.model


data class Userresponse(
    val error: Boolean,
    var message: String,
    var data: ArrayList<User>,
    var data2: ArrayList<FloodRequest>
)