package com.example.disaster.user

import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.disaster.R
import com.example.disaster.model.CommonResponse
import com.example.disaster.model.RetrofitClient
import com.example.disaster.toast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.util.Calendar

class DonerActivity : AppCompatActivity() {
    var encoded = ""
    lateinit var p: ProgressDialog
    lateinit var imag: ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_donor)
        val shared = getSharedPreferences("user", MODE_PRIVATE)
        val name = shared.getString("name", "")
        val id = shared.getInt("id", 0)

        val mail = shared.getString("email", "mail")
        val intent = intent.getStringExtra("num")
        val mobile = shared.getString("num", "") ?: intent


        p = ProgressDialog(this)
        p.setCancelable(false)


        val nam = findViewById<TextView>(R.id.name)
        val mailid2 = findViewById<TextView>(R.id.mailid2)
        val mobiles = findViewById<TextView>(R.id.mobile)
        val adress = findViewById<TextView>(R.id.adress)
        nam.text = name
        mailid2.text = mail
        mobiles.text = mobile


        imag = findViewById(R.id.seton)
        val discription = findViewById<EditText>(R.id.discription)
        val diases = findViewById<EditText>(R.id.disnmae)
        val submit = findViewById<Button>(R.id.submit)


        imag.setOnClickListener {
            val int = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(int, 100)
        }
        submit.setOnClickListener {
            p.show()
            val dis = discription.text.toString()
            val sometype = diases.text.toString()

            if (encoded == "") {
                p.dismiss()
                imag.toast("pleases Capture the Image Related to Donation")
            } else if (dis.isEmpty() || sometype == "") {
                p.dismiss()
                imag.toast("Please Fill the Form Neatly With Out Empty")
            } else if (dis.length <= 10) {
                p.dismiss()
                imag.toast("Please Type more than 10 words")
            } else {
                var date: String
                Calendar.getInstance().apply {
                    date = "${this.get(Calendar.DATE)}-${this.get(Calendar.MONTH) + 1}-${
                        this.get(Calendar.YEAR)
                    }"
                }
                val profile = ArrayList<String>()
                profile.add(name.toString())
                profile.add(mobile.toString())
                profile.add(mail.toString())
                profile.add(date)

                val text = profile.toString().split("[", "]")[1]


                CoroutineScope(IO).launch {
                    if (id != null) {
                        RetrofitClient.instance.uploadingactivity(
                            id = "$id",
                            name = "$name",
                            email = "$mail",
                            mobile = "$mobile",
                            description = "$dis",
                            type = sometype,
                            photo = encoded
                        ).enqueue(object : Callback<CommonResponse> {
                            override fun onResponse(
                                call: Call<CommonResponse>,
                                response: Response<CommonResponse>,
                            ) {

                                val responseData = response.body()!!
                                if (!responseData.error) {
                                    imag.toast(responseData.message)
                                    finish()
                                } else {
                                    imag.toast(response.body()!!.message)
                                }
                                p.dismiss()
                            }

                            override fun onFailure(call: Call<CommonResponse>, t: Throwable) {
                                imag.toast("${t.message}")
                                p.dismiss()
                            }
                        })
                    }
                }
            }
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data != null) {
            val bitmap = data.extras!!.get("data") as Bitmap
            val out = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
            val image = out.toByteArray()
            encoded = Base64.encodeToString(image, Base64.DEFAULT)
            val decode = Base64.decode(encoded, Base64.DEFAULT)
            Glide.with(this).load(decode).into(imag)
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        p.dismiss()
    }
}