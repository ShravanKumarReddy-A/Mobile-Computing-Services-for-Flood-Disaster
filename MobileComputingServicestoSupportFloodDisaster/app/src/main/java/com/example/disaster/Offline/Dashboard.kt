package com.example.disaster.Offline

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.example.disaster.databinding.ActivityDashboardBinding
import com.example.disaster.databinding.CardsafetyBinding
import com.google.android.material.bottomsheet.BottomSheetDialog

class Dashboard : AppCompatActivity() {
    private val b by lazy {
        ActivityDashboardBinding.inflate(layoutInflater)
    }
    private val bind by lazy {
        CardsafetyBinding.inflate(layoutInflater)
    }
    private val emergencyBinding by lazy {
        CardsafetyBinding.inflate(layoutInflater) // Use a separate binding for the emergency contact sheet
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(b.root)
        emergencyBinding.tvContact1.isVisible = false
        emergencyBinding.tvContact2.isVisible = false
        emergencyBinding.tvContact3.isVisible = false
        emergencyBinding.tvContact4.isVisible = false
        bind.tvinoformation.isVisible = false

        // Emergency contacts button click
        b.emergency.setOnClickListener {
            BottomSheetDialog(this).apply {
                setContentView(emergencyBinding.root)
                show()
                emergencyBinding.tvContact1.isVisible = true
                emergencyBinding.tvContact2.isVisible = true
                emergencyBinding.tvContact3.isVisible = true
                emergencyBinding.tvContact4.isVisible = true

                // Populate the contact list
                emergencyBinding.tvContact1.text = "NDRF: 1078"
                emergencyBinding.tvContact2.text = "Police: 100"
                emergencyBinding.tvContact3.text = "Ambulance: 108"
                emergencyBinding.tvContact4.text = "Fire Department: 101"

                // Call intent on contact click
                emergencyBinding.tvContact1.setOnClickListener {
                    makeCall("1078")
                }

                emergencyBinding.tvContact2.setOnClickListener {
                    makeCall("100")
                }

                emergencyBinding.tvContact3.setOnClickListener {
                    makeCall("108")
                }

                emergencyBinding.tvContact4.setOnClickListener {
                    makeCall("101")
                }
            }
        }

        // Safety information button
        b.btnsafety.setOnClickListener {
            BottomSheetDialog(this).apply {
                setContentView(bind.root)
                show()

                bind.tvinoformation.isVisible = true

                bind.tvinoformation.text = "" +
                        "1. Go on a higher area \n" +
                        "\n" +
                        "2. Do not enter deep waters or try to cross moving water roads \n" +
                        "\n" +
                        "3. Close electricity , stay way from electric poles \n" +
                        "\n" +
                        "4. Close LPG Cylinder \n" +
                        "\n" +
                        "5. keep first aid , clean water , food , mobile battery , solar chargers \n" +
                        "\n" +
                        "6. Inform police or NDRF \n" +
                        "\n" +
                        "7. After flood beware of diseases \n" +
                        "\n" +
                        "8. Do not panic , stay tuned to radio or tv , and follow government broadcast "
            }
        }

    }

    // Function to make a call
    private fun makeCall(phoneNumber: String) {
        val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$phoneNumber"))
        startActivity(intent)
    }
}
