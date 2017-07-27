package com.bridou_n.crossfitsolid.models

import com.google.gson.annotations.SerializedName

/**
 * Created by bridou_n on 27/07/2017.
 */

data class Address(@SerializedName("postalCode") val postalCode: String?,
                   @SerializedName("city") val city: String?,
                   @SerializedName("street") val street: String?,
                   @SerializedName("careOf") val careOf: String?,
                   @SerializedName("country") val country: Country?)