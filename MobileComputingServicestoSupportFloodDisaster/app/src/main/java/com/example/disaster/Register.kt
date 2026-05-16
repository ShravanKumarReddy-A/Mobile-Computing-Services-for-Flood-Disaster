package com.example.disaster

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.disaster.databinding.ActivityRegisterBinding
import com.example.disaster.model.DefaultResponse
import com.example.disaster.model.RetrofitClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Register : AppCompatActivity() {
    private val b by lazy {
        ActivityRegisterBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(b.root)

        val k = arrayOf("choose your choice", "User", "Donor", "Supports")

        ArrayAdapter(
            this@Register,
            android.R.layout.simple_list_item_checked, k
        ).apply {
            b.spinnertype.adapter = this
        }

        b.btnsubmit.setOnClickListener {
            val email = b.etemail.text.toString().trim()
            val name = b.etname.text.toString().trim()
            val address = b.etaddress.text.toString().trim()
            val city = b.etcity.text.toString().trim()
            val num = b.etmobilee.text.toString().trim()
            val pass = b.etpassword.text.toString().trim()
            val type = b.spinnertype.selectedItem.toString()


            if (email.isEmpty()) {
                b.etemail.error = "Enter your Email "
            } else if (name.isEmpty()) {
                b.etname.error = "Enter your name"
            } else if (address.isEmpty()) {
                b.etaddress.error = "Enter your Address"
            } else if (city.isEmpty()) {
                b.etcity.error = "Enter your city"
            } else if (num.isEmpty()) {
                b.etmobilee.error = "Enter your Mobile Number"
            } else if (pass.isEmpty()) {
                b.etpassword.error = "Enter your Password"
            } else if (num.count() != 10) {
                b.etmobilee.error = "Enter your mobile Number properly"
            } else if (type == "choose your choice") {
                Toast.makeText(this, "choose your choice properly", Toast.LENGTH_SHORT).show()
            } else {
                CoroutineScope(Dispatchers.IO).launch {
                    RetrofitClient.instance.register(
                        name,
                        num,
                        email,
                        address,
                        city,
                        pass,
                        type,
                        "Available",
                        "Not Verified",
                        "register"
                    )
                        .enqueue(object : Callback<DefaultResponse> {
                            override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                                Toast.makeText(this@Register, "" + t.message, Toast.LENGTH_SHORT)
                                    .show()
                            }

                            override fun onResponse(
                                call: Call<DefaultResponse>,
                                response: Response<DefaultResponse>,
                            ) {
                                val p1 = response.body()!!
                                if (p1.error) {
                                    Toast.makeText(
                                        this@Register,
                                        "${response.body()!!.message}",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    b.etname.text.clear()
                                    b.etmobilee.text.clear()
                                    b.etemail.text.clear()
                                    b.etaddress.text.clear()
                                    b.etcity.text.clear()
                                    b.etpassword.text.clear()
                                    finish()
                                    startActivity(Intent(this@Register, Login::class.java))

                                } else {
                                    Toast.makeText(
                                        this@Register,
                                        "${response.body()!!.message}",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }

                            }
                        })
                }
            }

        }

    }
}