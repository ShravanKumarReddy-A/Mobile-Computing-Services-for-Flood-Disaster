package com.example.disaster.user

import android.app.AlertDialog
import android.app.ProgressDialog
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.disaster.databinding.ActivityViewDonationsBinding
import com.example.disaster.model.DefaultResponse
import com.example.disaster.model.DoneeM
import com.example.disaster.model.DoneeStatus
import com.example.disaster.model.RetrofitClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ViewDonations : AppCompatActivity() {
    private val bind by lazy { ActivityViewDonationsBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(bind.root)
        val cycle = bind.cycle35
        cycle.layoutManager = LinearLayoutManager(this)
        val p = ProgressDialog(this).apply {
            setCancelable(false)
            show()
        }
        val g = getSharedPreferences("user", MODE_PRIVATE).getInt("id", 0)
        val name = getSharedPreferences("user", MODE_PRIVATE).getString("name", "0")

        updateData(p,g,name)

    }

    private fun updateData(p: ProgressDialog, g: Int, name: String?) {
        CoroutineScope(IO).launch {
            RetrofitClient.instance.gettingdetails().enqueue(object : Callback<DoneeStatus> {
                override fun onResponse(call: Call<DoneeStatus>, response: Response<DoneeStatus>) {

                    val list = response.body()!!.data
                    bind.cycle35.adapter = LastAdapter(this@ViewDonations, ArrayList(list)) {
                        updateDialog(it, g, name,p)
                    }
                    p.dismiss()
                }

                override fun onFailure(call: Call<DoneeStatus>, t: Throwable) {
                    Toast.makeText(this@ViewDonations, "${t.message}", Toast.LENGTH_SHORT).show()
                    p.dismiss()
                }
            })
        }
    }

    private fun updateDialog(it: DoneeM, g: Int, name: String?, p: ProgressDialog) {
        AlertDialog.Builder(this).apply {
            setCancelable(false)
            setTitle("Do you want close the Assigned Order")
            setMessage("Press Yes to cancel or no to cancel")
            setPositiveButton("Yes") { dialog, which ->
                updatedetails(it, g, name,p)
            }
            setNegativeButton("No") { dialog, which ->
                dialog.dismiss()
            }
            show()
        }

    }

    private fun updatedetails(it: DoneeM, g: Int, name: String?, p: ProgressDialog) {
        RetrofitClient.instance.updateDonationDetails(
            id = it.id,
            status = "Accepted",
            receiverId = "$g",
            receiverName = "$name"
        ).enqueue(object : Callback<DefaultResponse?> {
            override fun onResponse(
                call: Call<DefaultResponse?>,
                response: Response<DefaultResponse?>,
            ) {
                val body = response.body()!!
                if (!body.error) {
                    Toast.makeText(this@ViewDonations, body.message, Toast.LENGTH_SHORT).show()
                    updateData(p,g,name)
                } else {
                    Toast.makeText(this@ViewDonations, body.message, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<DefaultResponse?>, t: Throwable) {
                Toast.makeText(this@ViewDonations, t.message, Toast.LENGTH_SHORT).show()
            }
        })

    }
}