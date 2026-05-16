package com.example.disaster.user

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.text.HtmlCompat
import androidx.core.text.HtmlCompat.FROM_HTML_MODE_LEGACY
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.disaster.R
import com.example.disaster.model.CommonResponse
import com.example.disaster.model.DoneeM
import com.example.disaster.model.RetrofitClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LastAdapter(val context: Context, val list: ArrayList<DoneeM>,val updateitem:(DoneeM)->Unit) :
    RecyclerView.Adapter<LastAdapter.vieew>() {
    class vieew(item: View) : RecyclerView.ViewHolder(item) {
        val image = item.findViewById<ImageView>(R.id.imageView)
        val head = item.findViewById<TextView>(R.id.textview)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): vieew {
        return vieew(LayoutInflater.from(context).inflate(R.layout.mycard, parent, false))
    }

    override fun onBindViewHolder(holder: vieew, position: Int) {
        val last = list[position]

        Glide.with(context).load(last.photo).into(holder.image)

        var string = ""
        if (last.receiverId != "") {
            string = "<b><u>Donor</u></b><br>" +
                    "Name : ${last.name}<br>" +
                    "Mobile Number : ${last.mobile}<br>" +
                    "Mail-id : ${last.email}<br>" +
                    "<b><u>Donee Info</u></b><br>" +
                    "Receiver Name : ${last.receiverName}<br>" +
                    "<b><u>Status --></u></b> : ${last.someField}"
        } else {
            string = "<b><u>Donor Info</u></b><br>" +
                    "Name : ${last.name}<br>" +
                    "Mobile : ${last.mobile}<br>" +
                    "Mail-id : ${last.email}<br>"
        }
        holder.head.text = HtmlCompat.fromHtml(string, FROM_HTML_MODE_LEGACY)
        holder.itemView.setOnClickListener {
            if (last.someField != "Approved") {
                updateitem(last)
            }else{
                Toast.makeText(holder.itemView.context, "This has been accepted by the other user", Toast.LENGTH_SHORT).show()
            }

        }


    }

    private fun updatedetails(id: Int) {
        val ngo = context.getSharedPreferences("data", 0)
        val k = ngo.getString("name", "")!!.split(",")
        CoroutineScope(IO).launch {
            RetrofitClient.instance.ngoadedd(
                "NGO",
                id,
                "Completed by ${k[0]} NGO and it is Maintained by ${k[1]} Located in(${k[2]})\n Contact : ${
                    ngo.getString(
                        "mobile",
                        "Not Mentioned"
                    )
                }"
            ).enqueue(object : Callback<CommonResponse> {
                override fun onResponse(
                    call: Call<CommonResponse>,
                    response: Response<CommonResponse>,
                ) {
                    Toast.makeText(context, response.body()!!.message, Toast.LENGTH_SHORT).show()
                }

                override fun onFailure(call: Call<CommonResponse>, t: Throwable) {
                    Toast.makeText(context, "${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}