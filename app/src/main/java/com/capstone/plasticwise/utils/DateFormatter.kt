package com.capstone.plasticwise.utils

import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

fun DateFormatter (dateStr: String?) : String {
    if (dateStr == null) return ""
    val sdf = SimpleDateFormat("yyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
    sdf.timeZone = TimeZone.getTimeZone("UTC")
    val date = sdf.parse(dateStr) ?: return  ""

    val outputFormat = SimpleDateFormat("EEEE, dd MMMM yyyy", Locale("id", "ID"))
    return outputFormat.format(date)
}