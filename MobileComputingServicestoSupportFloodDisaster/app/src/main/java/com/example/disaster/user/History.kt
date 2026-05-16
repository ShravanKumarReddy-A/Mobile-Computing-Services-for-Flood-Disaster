package com.example.disaster.user

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.disaster.databinding.ActivityHistoryBinding
import com.example.disaster.model.FloodRequest
import com.example.disaster.model.LoginResponse
import com.example.disaster.model.RetrofitClient
import com.example.disaster.model.Userresponse
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class History<DialogInterface> : AppCompatActivity() {
    private val b by lazy {
        ActivityHistoryBinding.inflate(layoutInflater)
    }
    private val shared by lazy { getSharedPreferences("user", MODE_PRIVATE) }
    var id = ""
    var role = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(b.root)
        id = "${shared.getInt("id", 0)}"
        role = "${shared.getString("role", "")}"
        Toast.makeText(this, "$id", Toast.LENGTH_SHORT).show()

        loadData()


    }

    private fun loadData() {
        RetrofitClient.instance.getDonationRequests().enqueue(object : Callback<Userresponse?> {
            override fun onResponse(call: Call<Userresponse?>, response: Response<Userresponse?>) {
                val responseData = response.body()!!
                if (!responseData.error) {
                    Log.d("dlfjlkdjfljd", "djfhsjkfhjkhkJHD:${responseData.data2} ")

                    val list = if (role == "user") {
                        responseData.data2.filter { it.user_id == id }
                    } else {
                        responseData.data2
                    }

                    Log.d("fkdjhfijhdf", "onResponse:$list ")
                    val adapter = AdapterForUserStatus(
                        context = this@History,
                        data = ArrayList(list),
                        li = {
                            if (role != "user") {
                                openDialog(it)
                            } else {
                                Toast.makeText(this@History, "${it.status}", Toast.LENGTH_SHORT)
                                    .show()

                            }

                        }
                    )
                    b.rvlist.adapter = adapter
                    b.rvlist.layoutManager = LinearLayoutManager(this@History)
                } else {
                    Toast.makeText(this@History, "Failed to load the data", Toast.LENGTH_SHORT)
                        .show()
                }
            }

            override fun onFailure(call: Call<Userresponse?>, t: Throwable) {
                Toast.makeText(this@History, t.message!!, Toast.LENGTH_SHORT).show()
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
                        Toast.makeText(this@History, "Updated", Toast.LENGTH_SHORT).show()
                        dialog.dismiss()
                    } else {
                        Toast.makeText(this@History, "Failed", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<LoginResponse?>, t: Throwable) {
                    Toast.makeText(this@History, t.message!!, Toast.LENGTH_SHORT).show()
                }
            })

    }
}


