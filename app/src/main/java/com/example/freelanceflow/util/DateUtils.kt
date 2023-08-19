package com.example.freelanceflow.util

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


object DateUtils {
    private val sdf = SimpleDateFormat("MM/yyyy", Locale.getDefault())
    fun dateToString(date: Date?): String {
        return sdf.format(date)
    }

    fun stringToDate(dateString: String?): Date {
        return try {
            sdf.parse(dateString)
        } catch (e: ParseException) {
            e.printStackTrace()
            Date(0)
        }
    }
}