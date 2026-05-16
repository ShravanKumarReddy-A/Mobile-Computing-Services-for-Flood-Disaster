package com.example.disaster.user

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Base64
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.disaster.databinding.ActivityAddrequestBinding
import com.example.disaster.model.LoginResponse
import com.example.disaster.model.RetrofitClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Date

class Addrequest : AppCompatActivity() {

    private val bind by lazy { ActivityAddrequestBinding.inflate(layoutInflater) }
    var encode = ""
    private var selectedImageUri: Uri? = null
    val shared by lazy { getSharedPreferences("user", MODE_PRIVATE) }
    var id = ""
    var name = ""
    var phone = ""
    var location = ""

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(bind.root)
        name = shared.getString("name", "")!!
        phone = shared.getString("mobile", "")!!
        location = shared.getString("location", "")!!

        id = "${shared.getInt("id", 0)}" ?: "2"

        // Setup spinner items
        val requestTypes = arrayOf("Food", "Shelter", "Medical Help", "Rescue", "Other")
        val adapter =
            ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, requestTypes)
        bind.spinnertype.adapter = adapter

        // Handle image selection
        bind.imagadd.setOnClickListener {
            openImagePicker()
        }

        // Submit request
        bind.btnsubmit.setOnClickListener {
            submitFloodRequest()
        }
    }

    private fun openImagePicker() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_PICK_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == IMAGE_PICK_CODE && resultCode == RESULT_OK && data != null) {
            selectedImageUri = data.data
            bind.imagadd.setImageURI(selectedImageUri)
        }
    }

    private fun submitFloodRequest() {
        val location = bind.etemail.text.toString().trim()
        val description = bind.etDescription.text.toString().trim()
        val neededThings = bind.etname.text.toString().trim()
        val requestType = bind.spinnertype.selectedItem.toString()

        if (description.isEmpty() || neededThings.isEmpty() || selectedImageUri == null) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            return
        }


        CoroutineScope(IO).launch {
            contentResolver.openInputStream(selectedImageUri!!)?.readBytes()?.let {
                RetrofitClient.instance.sendRequest(
                    user_id = id,
                    name = name,
                    phone = phone,
                    location = "$description,$neededThings,$requestType,$location",
                    status = "Pending",
                    created_at = Date(System.currentTimeMillis()).toString(),
                    isVerified = "false",
                    image = Base64.encodeToString(it, Base64.DEFAULT)
                )
                    .enqueue(object : Callback<LoginResponse> {
                        override fun onResponse(
                            call: Call<LoginResponse>,
                            response: Response<LoginResponse>,
                        ) {
                            if (response.isSuccessful && response.body()?.error != true) {
                                Toast.makeText(
                                    this@Addrequest,
                                    "Request submitted!",
                                    Toast.LENGTH_LONG
                                ).show()
                                finish()
                            } else {
                                Toast.makeText(
                                    this@Addrequest,
                                    "Failed to submit",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        }

                        override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                            Toast.makeText(
                                this@Addrequest,
                                "Request failed: ${t.message}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    })
            }

        }


    }

    companion object {
        private const val IMAGE_PICK_CODE = 1000
    }
}
