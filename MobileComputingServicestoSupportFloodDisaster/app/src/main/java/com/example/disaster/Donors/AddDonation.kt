package com.example.disaster.Donors

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.disaster.databinding.ActivityAddDonationBinding

class AddDonation : AppCompatActivity() {
    private val b by lazy {
        ActivityAddDonationBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(b.root)

    }
}