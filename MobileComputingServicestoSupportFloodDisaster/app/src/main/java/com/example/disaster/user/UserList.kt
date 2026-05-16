package com.example.disaster.user

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.disaster.databinding.ActivityUserListBinding
import com.example.disaster.databinding.CarduseradminBinding
import com.example.disaster.model.RetrofitClient
import com.example.disaster.model.User
import com.example.disaster.model.Userresponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserList : AppCompatActivity() {
    private val bind by lazy { ActivityUserListBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(bind.root)

        CoroutineScope(IO).launch {
            RetrofitClient.instance.adminuser("User").enqueue(object : Callback<Userresponse?> {
                override fun onResponse(
                    call: Call<Userresponse?>,
                    response: Response<Userresponse?>,
                ) {
                    val p1 = response.body()!!
                    if (!p1.error) {
                        val list = p1.data
                        val adapter = userAdminAdapter(this@UserList, list)
                        bind.list.adapter = adapter
                        bind.list.layoutManager = LinearLayoutManager(this@UserList)

                    } else {
                        Toast.makeText(this@UserList, "Error Occurred", Toast.LENGTH_SHORT).show()
                    }

                }

                override fun onFailure(call: Call<Userresponse?>, t: Throwable) {
                    Toast.makeText(this@UserList, "Error", Toast.LENGTH_SHORT).show()
                }
            })

        }


    }


    inner class userAdminAdapter(var context: Context, var listdata: ArrayList<User>) :
        RecyclerView.Adapter<userAdminAdapter.DataViewHolder>() {

        inner class DataViewHolder(val view: CarduseradminBinding) :
            RecyclerView.ViewHolder(view.root)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder {
            return DataViewHolder(
                CarduseradminBinding.inflate(
                    LayoutInflater.from(context), parent, false
                )
            )
        }

        override fun onBindViewHolder(
            holder: DataViewHolder,
            @SuppressLint("RecyclerView") position: Int,
        ) {
            with(holder.view) {

                listdata[position].apply {
                    tvfname.text = name
                    tvfemail.text = email
                    tvfnum.text = num
                    tvfcity.text = city
                    if (type == "User") {
                        btndelete.visibility = View.GONE
                        tvtimimg.visibility = View.GONE
                        tvdescri.visibility = View.GONE
                        btnfeedback.visibility = View.GONE
                    }

                }

            }


        }


        override fun getItemCount() = listdata.size
    }
}