package com.example.disaster.Offline

import android.content.ClipData
import android.content.ClipboardManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.disaster.databinding.ActivityEmergencylistBinding

class Emergencylist : AppCompatActivity() {
    private val b by lazy {
        ActivityEmergencylistBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(b.root)

        val textContent = """
            Hyderabad / Telangana Flood helpline For any emergency Dial 100 / 112
            
            Toll Free Number for Emergency Services: 211111111
            
            GHMC Disaster Dept Emergency: 9000113667
            
            GHMC tree cutting: 6309062583
            
            GHMC ( water logging ): 9000113667
            
            NDRF : 8333068536
            
            MCH Disaster helpline : 97046018166 DISASTER HELPLINE : 112
            
            NDRF HELPLINE : 9711077372 , 011-24363260
            
            NDMA - 011-1078 , 011-26701700
        """.trimIndent()
        b.tvcontact.setOnClickListener {
            // Use regex to extract all numbers
            val regex = Regex("\\b\\d+\\b")
            val numbers = regex.findAll(textContent).map { it.value }.joinToString("\n")

            // Copy to clipboard
            val clipboard = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("Copied Numbers", numbers)
            clipboard.setPrimaryClip(clip)

            // Show toast message
            Toast.makeText(this, "Numbers copied to clipboard:\n$numbers", Toast.LENGTH_SHORT)
                .show()
        }


    }
}