package com.example.disaster.Admin

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.disaster.R
import com.example.disaster.databinding.ActivityAdminsupportsBinding
import com.example.disaster.databinding.CarduseradminBinding
import com.example.disaster.model.DefaultResponse
import com.example.disaster.model.RetrofitClient
import com.example.disaster.model.User
import com.example.disaster.model.Userresponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Adminsupports : AppCompatActivity() {
    private val bind by lazy { ActivityAdminsupportsBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(bind.root)

        readsupports()
    }

    private fun readsupports() {
        CoroutineScope(IO).launch {
            RetrofitClient.instance.adminuser("Supporter")
                .enqueue(object : Callback<Userresponse?> {
                    override fun onResponse(
                        call: Call<Userresponse?>,
                        response: Response<Userresponse?>,
                    ) {
                        val p1 = response.body()!!
                        if (!p1.error) {
                            val list = p1.data
                            val adapter = supportAdminAdapter(this@Adminsupports, list)
                            bind.listsupports.adapter = adapter
                            bind.listsupports.layoutManager =
                                LinearLayoutManager(this@Adminsupports)

                        } else {
                            Toast.makeText(this@Adminsupports, "Error Occurred", Toast.LENGTH_SHORT)
                                .show()
                        }

                    }

                    override fun onFailure(call: Call<Userresponse?>, t: Throwable) {
                        Toast.makeText(this@Adminsupports, "Error", Toast.LENGTH_SHORT).show()
                    }
                })

        }


    }


    inner class supportAdminAdapter(var context: Context, var listdata: ArrayList<User>) :
        RecyclerView.Adapter<supportAdminAdapter.DataViewHolder>() {

        inner class DataViewHolder(val view: CarduseradminBinding) :
            RecyclerView.ViewHolder(view.root)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder {
            return DataViewHolder(
                CarduseradminBinding.inflate(
                    LayoutInflater.from(context), parent,
                    false
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
                    tvtimimg.text = status



                    btndelete.setOnClickListener {
                        val alertdialog = AlertDialog.Builder(context)
                        alertdialog.setTitle("Delete")
                        alertdialog.setIcon(R.drawable.logo)
                        alertdialog.setCancelable(false)
                        alertdialog.setMessage("Do you Deleted the Profile?")
                        alertdialog.setPositiveButton("Yes") { alertdialog, which ->

                            deletesupport(id)
                            alertdialog.dismiss()
                        }

                        alertdialog.show()

                    }

                }

            }


        }

        private fun deletesupport(id: Int) {
            CoroutineScope(Dispatchers.IO).launch {
                RetrofitClient.instance.Deleteperson(id, "deletetable")
                    .enqueue(object : Callback<DefaultResponse> {
                        override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                            Toast.makeText(context, "" + t.message, Toast.LENGTH_SHORT).show()
                        }

                        override fun onResponse(
                            call: Call<DefaultResponse>,
                            response: Response<DefaultResponse>,
                        ) {
                            Toast.makeText(
                                context,
                                "${response.body()!!.message}",
                                Toast.LENGTH_SHORT
                            ).show()
                            readsupports()

                        }
                    })
            }

        }


        override fun getItemCount() = listdata.size
    }
}