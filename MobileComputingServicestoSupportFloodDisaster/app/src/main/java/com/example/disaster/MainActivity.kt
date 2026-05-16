package com.example.disaster

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.disaster.Offline.Dashboard
import com.example.disaster.databinding.ActivityMainBinding
import com.example.disaster.user.Userdashboard

class MainActivity : AppCompatActivity() {
    private val b by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val shared by lazy { getSharedPreferences("user", MODE_PRIVATE) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(b.root)

        b.tvemergency.setOnClickListener { startActivity(Intent(this, Dashboard::class.java)) }

        b.button.setOnClickListener {
            when (shared.getString("type", "")) {
                "User" -> startActivity(Intent(this, Userdashboard::class.java))
                "Donor" -> startActivity(Intent(this, DonorDashboard::class.java))
                "Supports" -> startActivity(Intent(this, SupportDashboard::class.java))
                "admin" -> startActivity(Intent(this, AdminDashboard::class.java))
                else -> startActivity(Intent(this, Login::class.java))
            }

        }
    }
}