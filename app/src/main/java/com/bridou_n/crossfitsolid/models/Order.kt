package com.bridou_n.crossfitsolid.models

import com.google.gson.annotations.SerializedName

/**
 * Created by bridou_n on 27/07/2017.
 */

data class Order(@SerializedName("id") val id: Int?,
                 @SerializedName("number") val number: Int?)