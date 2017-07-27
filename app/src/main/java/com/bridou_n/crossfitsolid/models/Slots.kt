package com.bridou_n.crossfitsolid.models

import com.google.gson.annotations.SerializedName

/**
 * Created by bridou_n on 27/07/2017.
 */

data class Slots(@SerializedName("total") val total: Int?,
                 @SerializedName("totalBookable") val totalBookable: Int?,
                 @SerializedName("reservedForDropin") val reservedForDropin: Int?,
                 @SerializedName("leftToBook") val leftToBook: Int?,
                 @SerializedName("hasWaitingList") val hasWaitingList: Boolean?,
                 @SerializedName("inWaitingList") val inWaitingList: Int?)