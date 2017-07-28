package com.bridou_n.crossfitsolid.utils.extensionFunctions

import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by bridou_n on 27/07/2017.
 */

fun getIso8601Format() : String = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"

// This is super akward, the date we receive is in GMT+2 BUT is 2h earlier then it should..
fun getTimeZone() = TimeZone.getTimeZone("GMT+4")

fun Date.toIso8601Format() : String {
    val sdf: SimpleDateFormat = SimpleDateFormat(getIso8601Format(), Locale.US)

    sdf.timeZone = TimeZone.getTimeZone("GMT")
    return sdf.format(this)
}

fun Date.fromIso8601Format(dateString: String) : Date {
    val sdf: SimpleDateFormat = SimpleDateFormat(getIso8601Format(), Locale.US)

    sdf.timeZone = getTimeZone()
    return sdf.parse(dateString)
}

fun Date.getDayString() : String {
    val sdf = SimpleDateFormat("EEEE", Locale.getDefault())

    sdf.timeZone = getTimeZone()
    return sdf.format(this)
}

fun Date.getFullDate() : String {
    val df = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.getDefault())

    return df.format(this)
}

fun Date.getMonthString() : String {
    val sdf = SimpleDateFormat("MMM", Locale.getDefault())

    sdf.timeZone = getTimeZone()
    return sdf.format(this)
}

fun Date.getHourMinute() : String {
    val sdf = SimpleDateFormat("HH:mm", Locale.US)

    sdf.timeZone = getTimeZone()
    return sdf.format(this)
}