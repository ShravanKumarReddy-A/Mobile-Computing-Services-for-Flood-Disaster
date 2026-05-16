package com.example.disaster.user

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.disaster.databinding.ActivityViewsupportsBinding

class Viewsupports : AppCompatActivity() {
    private val b by lazy {
        ActivityViewsupportsBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(b.root)

    }
}