package com.example.disaster.user

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.text.HtmlCompat
import androidx.core.text.HtmlCompat.FROM_HTML_MODE_LEGACY
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.disaster.R
import com.example.disaster.model.FloodRequest

class AdapterForUserStatus(
    val context: Context,
    val data: ArrayList<FloodRequest>,
    val li: (FloodRequest) -> Unit,
) :
    RecyclerView.Adapter<AdapterForUserStatus.viewed>() {
    class viewed(item: View) : RecyclerView.ViewHolder(item) {
        val image = item.findViewById<ImageView>(R.id.imageView)
        val head = item.findViewById<TextView>(R.id.textview)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewed {
        val view = LayoutInflater.from(context).inflate(R.layout.view_historycard, parent, false)
        return viewed(view)
    }

    @SuppressLint("CheckResult", "SetTextI18n")
    override fun onBindViewHolder(holder: viewed, position: Int) {
        val data = data[position]

        Glide.with(context).load(data.image).into(holder.image)
        holder.image.setOnClickListener {
            li(data)

        }
        val split = data.location.split(",")
        val thestring = "<b>Id -></b> MD${data.id}<br></br>" +
                "<b>Name -></b> ${data.name}<br></br>" +
                "<b>Help Type -></b> ${split[1]}<br></br>" +
                "<b>Location -></b> ${split[3]}<br></br>" +
                "<b>Description -></b> ${split[0]}<br></br>" +
                "<b>RequestData-></b> ${data.created_at}<br></br>" +
                "<b>Status -></b> ${data.status}<br></br>"

        holder.head.apply {
            setText(
                HtmlCompat.fromHtml(thestring, FROM_HTML_MODE_LEGACY)
            )
            setOnClickListener {
                //li.click2(data[position])
            }
        }

        holder.itemView.setOnClickListener {
            li(data)
        }


    }

    override fun getItemCount(): Int {
        return data.size
    }
}