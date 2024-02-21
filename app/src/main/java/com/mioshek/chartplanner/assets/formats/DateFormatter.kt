package com.mioshek.chartplanner.assets.formats

import android.annotation.SuppressLint
import java.text.SimpleDateFormat

class DateFormatter {
    companion object{
        @SuppressLint("SimpleDateFormat")
        val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
    }
}