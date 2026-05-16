package com.example.disaster

import android.app.Activity
import android.text.Spanned
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.text.HtmlCompat

fun View.toast(message: String) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}

fun logsucces(message: String) {
    Log.i("MedicineDonorSucess", message)
}

fun logfail(message: String) {
    Log.i("MedicineDonorFail", message)
}

fun Activity.showToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun spanned(text: String): Spanned {
    return HtmlCompat.fromHtml(text, HtmlCompat.FROM_HTML_OPTION_USE_CSS_COLORS)
}