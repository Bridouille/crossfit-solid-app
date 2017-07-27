package com.bridou_n.crossfitsolid.models

import com.google.gson.annotations.SerializedName

/**
 * Created by bridou_n on 27/07/2017.
 */

data class GroupActivityBooking(@SerializedName("type") val type: String?,
                                @SerializedName("groupActivity") val groupActivity: GroupActivitySummary?,
                                @SerializedName("businessUnit") val businessUnit: Business?,
                                @SerializedName("customer") val customer: Profile?,
                                @SerializedName("duration") val duration: Duration?,
                                @SerializedName("groupActivityBooking") val groupActivityBooking: GroupActivityBookingSummary?,
                                @SerializedName("additionToEventBooking") val additionToEventBooking: String?)