package com.example.disaster

import android.content.Intent
import android.os.Bundle
import android.provider.Telephony
import android.telephony.SmsManager
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.disaster.Admin.Admindonors
import com.example.disaster.Admin.Adminsupports
import com.example.disaster.databinding.ActivityAdminDashboardBinding
import com.example.disaster.databinding.CardviewusersBinding
import com.example.disaster.model.RetrofitClient
import com.example.disaster.model.User
import com.example.disaster.model.Userresponse
import com.example.disaster.model.logout
import com.example.disaster.user.History
import com.example.disaster.user.UserList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import kotlin.math.log


class AdminDashboard : AppCompatActivity() {
    private val b by lazy {
        ActivityAdminDashboardBinding.inflate(layoutInflater)
    }
    private val bind by lazy {
        CardviewusersBinding.inflate(layoutInflater)
    }
    var number = ""
    private val list = ArrayList<User>()
    private lateinit var progressDialog: AlertDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(b.root)

        CoroutineScope(IO).launch {
            RetrofitClient.instance.adminuser("User").enqueue(object : Callback<Userresponse?> {
                override fun onResponse(
                    call: Call<Userresponse?>,
                    response: Response<Userresponse?>,
                ) {
                    val p1 = response.body()!!
                    if (!p1.error) {
                      list.addAll(ArrayList(p1.data))


                    } else {
                        Toast.makeText(this@AdminDashboard, "Error Occurred", Toast.LENGTH_SHORT).show()
                    }

                }

                override fun onFailure(call: Call<Userresponse?>, t: Throwable) {
                    Toast.makeText(this@AdminDashboard, "Error", Toast.LENGTH_SHORT).show()
                }
            })

        }

        b.btnadminlogout.setOnClickListener { logout() }
        b.verifyRequest.setOnClickListener {
            startActivity(Intent(this@AdminDashboard, History::class.java))
        }
        b.sendalert.setOnClickListener {
            val smsManager: SmsManager = SmsManager.getDefault()
            list.forEach {
                Log.d("kdfsjkfh", "the numbers: ${it.num}")

                smsManager.sendTextMessage(
                    "${it.num}",
                    null,
                    "Flood Alert! Take necessary precautions. Avoid flooded areas, move to higher ground, and follow local authorities' instructions",
                    null,
                    null
                )
            }
            Toast.makeText(this@AdminDashboard, "Alert Sent to All Users", Toast.LENGTH_SHORT).show()

            // Ensure the list of users is fetched before sending the alert

        }

        b.btnadminusers.setOnClickListener { startActivity(Intent(this, UserList::class.java)) }
        b.btnadmindonors.setOnClickListener { startActivity(Intent(this, Admindonors::class.java)) }
        b.btnadminsupports.setOnClickListener {
            startActivity(
                Intent(
                    this,
                    Adminsupports::class.java
                )
            )
        }

    }


}