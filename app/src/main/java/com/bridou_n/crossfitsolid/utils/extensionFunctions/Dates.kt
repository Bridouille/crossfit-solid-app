package com.bridou_n.crossfitsolid.utils.extensionFunctions

import org.joda.time.LocalDate
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by bridou_n on 27/07/2017.
 */

fun getIso8601Format() : String = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"

fun Date.toIso8601Format() : String {
    val sdf: SimpleDateFormat = SimpleDateFormat(getIso8601Format(), Locale.US)

    return sdf.format(this)
}

fun Date.getDayString() : String {
    val sdf = SimpleDateFormat("EEEE", Locale.getDefault())

    return sdf.format(this)
}

fun Date.getFullDate() : String {
    val df = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.getDefault())

    return df.format(this)
}

fun Date.getFullDateAndTime() : String {
    val df = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT, Locale.getDefault())

    return df.format(this)
}

fun Date.getHourMinute() : String {
    val sdf = SimpleDateFormat("HH:mm", Locale.US)

    return sdf.format(this)
}

fun LocalDate.isToday() : Boolean = this.compareTo(LocalDate()) == 0