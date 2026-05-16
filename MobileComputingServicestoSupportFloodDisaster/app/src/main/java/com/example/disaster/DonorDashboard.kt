package com.example.disaster

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.text.InputFilter
import android.text.TextUtils
import android.util.Log
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.disaster.databinding.ActivityDonorDashboardBinding
import com.example.disaster.databinding.CardprofiledonationsBinding
import com.example.disaster.model.DefaultResponse
import com.example.disaster.model.DoneeStatus
import com.example.disaster.model.RetrofitClient
import com.example.disaster.model.logout
import com.example.disaster.user.DonerActivity
import com.example.disaster.user.LastAdapter
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DonorDashboard : AppCompatActivity() {
    private val b by lazy { ActivityDonorDashboardBinding.inflate(layoutInflater) }
    private val bind by lazy { CardprofiledonationsBinding.inflate(layoutInflater) }
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

        b.fab.setOnClickListener {
            startActivity(Intent(this@DonorDashboard, DonerActivity::class.java).apply {
                putExtra("num", num)
            })
        }

        val k = arrayOf("choose your choice", "Available", "Not Available")

        ArrayAdapter(
            this@DonorDashboard,
            android.R.layout.simple_list_item_checked, k
        ).apply {
            bind.spinstatus.adapter = this
        }

        k.forEachIndexed { index, s ->
            if (s == status) {
                bind.spinstatus.setSelection(index, true)
            }
        }

        val cycle = b.image
        cycle.layoutManager = LinearLayoutManager(this)
        val p = ProgressDialog(this).apply {
            setCancelable(false)
            show()
        }
        val g = getSharedPreferences("user", MODE_PRIVATE).getInt("id", 0)!!
        Log.d("fdhfkjhdsf", "id: $g")

        CoroutineScope(IO).launch {
            RetrofitClient.instance.gettingdetails().enqueue(object : Callback<DoneeStatus> {
                override fun onResponse(call: Call<DoneeStatus>, response: Response<DoneeStatus>) {
                    if (response.isSuccessful && response.body() != null) {
                        val list = response.body()!!.data.filter { it.donorId == "$g" }
                        Log.d("fkejfkjf", "onResponse: Data fetched successfully  $list")
                        cycle.adapter = LastAdapter(this@DonorDashboard, ArrayList(list)){

                        }
                        p.dismiss()
                    } else {
                        // Handle case when response body is null or request fails
                        Log.e("Error", "Failed to fetch data: ${response.message()}")
                        Toast.makeText(
                            this@DonorDashboard,
                            "Error fetching data",
                            Toast.LENGTH_SHORT
                        ).show()
                        p.dismiss()
                    }
                }

                override fun onFailure(call: Call<DoneeStatus>, t: Throwable) {
                    Log.e("Error", "Network request failed: ${t.message}")
                    Toast.makeText(
                        this@DonorDashboard,
                        "Network error: ${t.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                    p.dismiss()
                }
            })
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

        b.imageView4.setOnClickListener {
            val popupMenu: PopupMenu = PopupMenu(this, b.imageView4)
            popupMenu.menuInflater.inflate(R.menu.menu, popupMenu.menu)
            popupMenu.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.action_profile -> {
                        popupMenu.dismiss()
                        BottomSheetDialog(this).apply {
                            (bind.root.parent as? ViewGroup)?.removeView(bind.root)
                            setContentView(bind.root)
                            /// profile
                            bind.btnsubmit.setOnClickListener {
                                val name1 = bind.etname.text.toString().trim()
                                val address1 = bind.etaddress.text.toString().trim()
                                val city1 = bind.etcity.text.toString().trim()
                                val num1 = bind.etmobilee.text.toString().trim()
                                val pass1 = bind.etpassword.text.toString().trim()
                                val status1 = bind.spinstatus.selectedItem.toString()

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
                                } else if (status1 == "choose your choice") {
                                    Toast.makeText(
                                        this@DonorDashboard,
                                        "choose your choice",
                                        Toast.LENGTH_SHORT
                                    ).show()
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
                                                        this@DonorDashboard,
                                                        "" + t.message,
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                }

                                                override fun onResponse(
                                                    call: Call<DefaultResponse>,
                                                    response: Response<DefaultResponse>,
                                                ) {
                                                    Toast.makeText(
                                                        this@DonorDashboard,
                                                        "${response.body()!!.message}",
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                    dismiss()
                                                    getSharedPreferences(
                                                        "user",
                                                        MODE_PRIVATE
                                                    ).edit().apply {
                                                        putString("num", num1)
                                                        putString("pass", pass1)
                                                        putString("email", email)
                                                        putString("name", name1)
                                                        putString("address", address1)
                                                        putString("type", type)
                                                        putString("status", status1)
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
                            /// profile
                            show()
                        }

                    }

                    R.id.action_Logout -> {
                        logout()
                        popupMenu.dismiss()
                    }

                }
                true
            })
            popupMenu.show()
        }
    }
}