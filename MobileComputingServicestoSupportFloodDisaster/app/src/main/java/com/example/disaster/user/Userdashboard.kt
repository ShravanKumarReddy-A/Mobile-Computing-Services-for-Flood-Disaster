package com.example.disaster.user

import android.content.Intent
import android.os.Bundle
import android.text.InputFilter
import android.text.TextUtils
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.disaster.databinding.ActivityUserdashboardBinding
import com.example.disaster.databinding.CarduserprofileBinding
import com.example.disaster.model.DefaultResponse
import com.example.disaster.model.RetrofitClient
import com.example.disaster.model.logout
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Userdashboard : AppCompatActivity() {
    private val b by lazy { ActivityUserdashboardBinding.inflate(layoutInflater) }
    private val bind by lazy { CarduserprofileBinding.inflate(layoutInflater) }

    var id = 0
    var name = ""
    var num = ""
    var email = ""
    var address = ""
    var city = ""
    var pass = ""
    var type = ""
    var status = ""
    var astatus = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(b.root)

        b.sendRequest.setOnClickListener {
            startActivity(Intent(this@Userdashboard, Addrequest::class.java))
        }

        b.btnwaterlevel.setOnClickListener {
            startActivity(Intent(this, History::class.java))
        }

        b.btndiet.setOnClickListener {

        }
        b.activity.setOnClickListener {
            startActivity(Intent(this,ViewDonations::class.java))
        }

        getSharedPreferences("user", AppCompatActivity.MODE_PRIVATE).apply {
            id = getInt("id", 0)
            name = getString("name", "").toString()
            num = getString("num", "").toString()
            email = getString("email", "").toString()
            address = getString("address", "").toString()
            city = getString("city", "").toString()
            pass = getString("pass", "").toString()
            type = getString("type", "").toString()
            status = getString("status", "").toString()
            astatus = getString("astatus", "").toString()

        }

        bind.etcity.setFilters(arrayOf<InputFilter>(InputFilter { source, start, end, dest, dstart, dend ->
            if (source.length > 0 && dstart == 0) {
                val v = CharArray(source.length)
                TextUtils.getChars(source, 0, source.length, v, 0)
                v[0] = v[0].uppercaseChar()
                return@InputFilter String(v)
            }
            null
        }
        ))

        bind.etname.setText(name)
        bind.etmobilee.setText(num)

        bind.etaddress.setText(address)
        bind.etcity.setText(city)
        bind.etpassword.setText(pass)

        b.tvname.text = "WELCOME $name"
        b.btnlogout.setOnClickListener { logout() }
        b.imagadd.setOnClickListener {
            BottomSheetDialog(this).apply {
                (bind.root.parent as? ViewGroup)?.removeView(bind.root)
                setContentView(bind.root)

                show()

                bind.btnsubmit.setOnClickListener {
                    val name1 = bind.etname.text.toString().trim()
                    val address1 = bind.etaddress.text.toString().trim()
                    val city1 = bind.etcity.text.toString().trim()
                    val num1 = bind.etmobilee.text.toString().trim()
                    val pass1 = bind.etpassword.text.toString().trim()


                    if (name1.isEmpty()) {
                        bind.etname.error = "Enter your name"
                    } else if (address1.isEmpty()) {
                        bind.etaddress.error = "Enter your Address"
                    } else if (city1.isEmpty()) {
                        bind.etcity.error = "Enter your city"
                    } else if (num1.isEmpty()) {
                        bind.etmobilee.error = "Enter your Mobile Number"
                    } else if (pass1.isEmpty()) {
                        bind.etpassword.error = "Enter your Password"
                    } else if (num1.count() != 10) {
                        bind.etmobilee.error = "Enter your mobile Number properly"
                    } else {
                        CoroutineScope(Dispatchers.IO).launch {
                            RetrofitClient.instance.updateprofile(
                                name1,
                                num1,
                                address1,
                                city1,
                                pass1,
                                type,
                                "",
                                id,
                                "register"
                            )
                                .enqueue(object : Callback<DefaultResponse> {
                                    override fun onFailure(
                                        call: Call<DefaultResponse>,
                                        t: Throwable,
                                    ) {
                                        Toast.makeText(
                                            this@Userdashboard,
                                            "" + t.message,
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }

                                    override fun onResponse(
                                        call: Call<DefaultResponse>,
                                        response: Response<DefaultResponse>,
                                    ) {
                                        Toast.makeText(
                                            this@Userdashboard,
                                            "${response.body()!!.message}",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        dismiss()
                                        getSharedPreferences("user", MODE_PRIVATE).edit().apply {
                                            putString("num", num1)
                                            putString("pass", pass1)
                                            putString("email", email)
                                            putString("name", name1)
                                            putString("address", address1)
                                            putString("type", type)
                                            putString("status", status)
                                            putString("city", city1)
                                            putString("astatus", astatus)
                                            putInt("id", id)
                                            apply()
                                        }
                                    }
                                })
                        }
                    }

                }
            }

        }

    }
}