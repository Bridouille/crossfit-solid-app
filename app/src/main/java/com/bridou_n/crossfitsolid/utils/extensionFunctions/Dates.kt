package com.bridou_n.crossfitsolid.utils.extensionFunctions

import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by bridou_n on 27/07/2017.
 */

fun Date.toIso8601Format() : String {
    val sdf: SimpleDateFormat = SimpleDateFormat(getIso8601Format(), Locale.US)

    sdf.timeZone = TimeZone.getTimeZone("UTC")
    return sdf.format(this)
}

fun Date.fromIso8601Format(dateString: String) : Date {
    val sdf: SimpleDateFormat = SimpleDateFormat(getIso8601Format(), Locale.US)

    sdf.timeZone = TimeZone.getTimeZone("UTC")
    return sdf.parse(dateString)
}

fun getIso8601Format() : String = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"