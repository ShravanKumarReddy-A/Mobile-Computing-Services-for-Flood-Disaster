package com.example.disaster

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
import com.example.disaster.databinding.ActivitySupportDashboardBinding
import com.example.disaster.databinding.CardprofiledonationsBinding
import com.example.disaster.model.DefaultResponse
import com.example.disaster.model.FloodRequest
import com.example.disaster.model.LoginResponse
import com.example.disaster.model.RetrofitClient
import com.example.disaster.model.Userresponse
import com.example.disaster.model.logout
import com.example.disaster.user.AdapterForUserStatus
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SupportDashboard : AppCompatActivity() {
    private val b by lazy {
        ActivitySupportDashboardBinding.inflate(layoutInflater)
    }
    private val bind by lazy {
        CardprofiledonationsBinding.inflate(layoutInflater)
    }
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
        loadData()



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

        val k = arrayOf("choose your choice", "Available", "Not Available")



        ArrayAdapter(
            this@SupportDashboard,
            android.R.layout.simple_list_item_checked, k
        ).apply {
            bind.spinstatus.adapter = this
        }
        k.forEachIndexed { index, s ->
            if (s == status) {
                bind.spinstatus.setSelection(index, true)
            }
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
                                        this@SupportDashboard,
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
                                                        this@SupportDashboard,
                                                        "" + t.message,
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                }

                                                override fun onResponse(
                                                    call: Call<DefaultResponse>,
                                                    response: Response<DefaultResponse>,
                                                ) {
                                                    Toast.makeText(
                                                        this@SupportDashboard,
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

    private fun loadData() {
        RetrofitClient.instance.getDonationRequests().enqueue(object : Callback<Userresponse?> {
            override fun onResponse(call: Call<Userresponse?>, response: Response<Userresponse?>) {
                val responseData = response.body()!!
                if (!responseData.error) {
                    Log.d("dlfjlkdjfljd", "djfhsjkfhjkhkJHD:${responseData.data2} ")

                    val list = responseData.data2


                    Log.d("fkdjhfijhdf", "onResponse:$list ")
                    val adapter = AdapterForUserStatus(
                        context = this@SupportDashboard,
                        data = ArrayList(list),
                        li = {
                            if (type != "user") {
                                openDialog(it)
                            } else {
                                Toast.makeText(
                                    this@SupportDashboard,
                                    "${it.status}",
                                    Toast.LENGTH_SHORT
                                )
                                    .show()

                            }

                        }
                    )
                    b.rvlist.adapter = adapter
                    b.rvlist.layoutManager = LinearLayoutManager(this@SupportDashboard)
                } else {
                    Toast.makeText(
                        this@SupportDashboard,
                        "Failed to load the data",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
            }

            override fun onFailure(call: Call<Userresponse?>, t: Throwable) {
                Toast.makeText(this@SupportDashboard, t.message!!, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun openDialog(it: FloodRequest) {
        MaterialAlertDialogBuilder(this)
            .setTitle("Confirm")
            .setMessage("Are you sure you want to update this Request")
            .setPositiveButton("Confirm") { dialog, _ ->
                updateRequest(it, dialog)
            }.setNegativeButton("Cancel") { dialgo, _ ->

            }.show()
    }

    private fun updateRequest(it: FloodRequest, dialog: android.content.DialogInterface) {
        RetrofitClient.instance.updateRequest("Approved", it.id)
            .enqueue(object : Callback<LoginResponse?> {
                override fun onResponse(
                    call: Call<LoginResponse?>,
                    response: Response<LoginResponse?>,
                ) {
                    val responseData = response.body()!!
                    if (!responseData.error) {
                        Toast.makeText(this@SupportDashboard, "Updated", Toast.LENGTH_SHORT).show()
                        dialog.dismiss()
                    } else {
                        Toast.makeText(this@SupportDashboard, "Failed", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<LoginResponse?>, t: Throwable) {
                    Toast.makeText(this@SupportDashboard, t.message!!, Toast.LENGTH_SHORT).show()
                }
            })

    }
}