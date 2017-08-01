package com.bridou_n.crossfitsolid.models.classes

import com.google.gson.annotations.SerializedName

/**
 * Created by bridou_n on 30/07/2017.
 */

data class BookingError(@SerializedName("errorCode") val errorCode: String?,
                        @SerializedName("perDay") val perDay: Int?,
                        @SerializedName("perWeek") val perWeek: Int?,
                        @SerializedName("total") val total: Int?,
                        @SerializedName("minutesBefore") val minutesBefore: Int?)