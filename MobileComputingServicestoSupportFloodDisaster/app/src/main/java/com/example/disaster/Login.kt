package com.example.disaster

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.disaster.databinding.ActivityLoginBinding
import com.example.disaster.model.LoginResponse
import com.example.disaster.model.RetrofitClient
import com.example.disaster.user.Userdashboard
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Login : AppCompatActivity() {
    private val b by lazy {
        ActivityLoginBinding.inflate(layoutInflater)
    }
    private val shared2 by lazy { getSharedPreferences("user", MODE_PRIVATE) }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(b.root)

        b.btnsignup.setOnClickListener {
            startActivity(
                Intent(
                    this, Register::class.java
                )
            )
        }



        b.btnsignin.setOnClickListener {
            var email = b.etemail.text.toString().trim()
            var pass = b.etpassword.text.toString().trim()


            if (email.isEmpty()) {
                b.etemail.error = "Enter your Email"
            } else if (pass.isEmpty()) {
                b.etpassword.error = "Enter your password"
            } else if (email.contains("admin@gmail.com") && pass.contains("admin")) {
                startActivity(Intent(this, AdminDashboard::class.java))
                shared2.edit().putString("type", "admin").apply()

                finish()
            } else {
                CoroutineScope(Dispatchers.IO).launch {
                    RetrofitClient.instance.login(email, pass, "login")
                        .enqueue(object : Callback<LoginResponse> {
                            override fun onResponse(
                                call: Call<LoginResponse>, response: Response<LoginResponse>,
                            ) {
                                if (!response.body()?.error!!) {
                                    response.body()?.data?.let {
                                        if (it.isEmpty()) {
                                            Toast.makeText(
                                                applicationContext,
                                                "Invalid User",
                                                Toast.LENGTH_SHORT
                                            )
                                                .show()
                                        } else {
                                            it[0].let { type ->
                                                getSharedPreferences("user", MODE_PRIVATE).edit()
                                                    .apply {
                                                        putString("num", type.num)
                                                        putString("pass", type.pass)
                                                        putString("email", type.email)
                                                        putString("name", type.name)
                                                        putString("address", type.address)
                                                        putString("type", type.type)
                                                        putString("status", type.status)
                                                        putString("city", type.city)
                                                        putString("astatus", type.astatus)
                                                        putInt("id", type.id)
                                                        apply()
                                                    }


                                                when (type.type) {
                                                    "User" -> {
                                                        startActivity(
                                                            Intent(
                                                                this@Login,
                                                                Userdashboard::class.java
                                                            )
                                                        )
                                                        finish()
                                                    }

                                                    "Donor" -> {
                                                        startActivity(
                                                            Intent(
                                                                this@Login,
                                                                DonorDashboard::class.java
                                                            )
                                                        )
                                                        finish()
                                                    }

                                                    "Supports" -> {

                                                        startActivity(
                                                            Intent(
                                                                this@Login,
                                                                SupportDashboard::class.java
                                                            )
                                                        )
                                                        finish()

                                                    }
                                                }

                                                Toast.makeText(
                                                    this@Login,
                                                    response.body()?.message,
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            }
                                        }
                                    }
                                } else {
                                    Toast.makeText(
                                        this@Login,
                                        response.body()?.message,
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }

                            }

                            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                                Toast.makeText(this@Login, t.message, Toast.LENGTH_LONG).show()


                            }

                        })
                }
            }
        }

    }
}