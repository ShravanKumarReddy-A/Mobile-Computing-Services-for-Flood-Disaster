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
import com.example.disaster.databinding.ActivityAdmindonorsBinding
import com.example.disaster.databinding.CarduseradminBinding
import com.example.disaster.model.DefaultResponse
import com.example.disaster.model.RetrofitClient
import com.example.disaster.model.User
import com.example.disaster.model.Userresponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Admindonors : AppCompatActivity() {
    private val b by lazy {
        ActivityAdmindonorsBinding.inflate(layoutInflater)
    }
    private lateinit var progressDialog: AlertDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(b.root)


        readdonors()

    }

    private fun readdonors() {
        val builder = AlertDialog.Builder(this, R.style.TransparentDialog)
        val inflater = this.layoutInflater
        builder.setView(inflater.inflate(R.layout.progressdialog, null))
        builder.setCancelable(false)
        progressDialog = builder.create()
        progressDialog.show()


        CoroutineScope(Dispatchers.IO).launch {
            RetrofitClient.instance.admindonors("Worker")
                .enqueue(object : Callback<Userresponse> {
                    @SuppressLint("SetTextI18n")
                    override fun onResponse(
                        call: Call<Userresponse>,
                        response: Response<Userresponse>,
                    ) {

                        b.lineardonors.let {
                            response.body()?.data?.let { it1 ->
                                it.adapter =
                                    DonorAdminAdapter(this@Admindonors, response.body()!!.data)
                                it.layoutManager = LinearLayoutManager(this@Admindonors)
                                Toast.makeText(this@Admindonors, "success", Toast.LENGTH_SHORT)
                                    .show()
                            }
                        }
                        progressDialog.dismiss()
                    }

                    override fun onFailure(call: Call<Userresponse>, t: Throwable) {
                        Toast.makeText(this@Admindonors, "${t.message}", Toast.LENGTH_SHORT).show()

                    }

                })
        }


    }

    inner class DonorAdminAdapter(
        var context: Context,
        var listdata: ArrayList<User>,
    ) :
        RecyclerView.Adapter<DonorAdminAdapter.DataViewHolder>() {

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

                            deletehospital(id)
                            alertdialog.dismiss()
                        }

                        alertdialog.show()

                    }

                }

            }


        }

        private fun deletehospital(id: Int) {
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
                            readdonors()

                        }
                    })
            }

        }


        override fun getItemCount() = listdata.size
    }
}