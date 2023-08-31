package com.example.freelanceflow.util

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


object DateUtils {
    lateinit var sdf: SimpleDateFormat
    fun dateToString(date: Date?, pattern: String = "MM/yyyy"): String {
        sdf = SimpleDateFormat(pattern, Locale.getDefault())
        return sdf.format(date)

    }

    fun stringToDate(dateString: String?, pattern: String = "MM/yyyy"): Date {
        return try {
            sdf = SimpleDateFormat(pattern, Locale.getDefault())
            sdf.parse(dateString)
        } catch (e: ParseException) {
            e.printStackTrace()
            Date(0)
        }
    }
}