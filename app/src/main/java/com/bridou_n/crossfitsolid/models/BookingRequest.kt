package com.bridou_n.crossfitsolid.models

import com.google.gson.annotations.SerializedName

/**
 * Created by bridou_n on 29/07/2017.
 */

class BookingRequest(@SerializedName("groupActivity") val groupActivityId: Int?,
                     @SerializedName("allowWaitingList") val allowWaitingList: Boolean? = false)