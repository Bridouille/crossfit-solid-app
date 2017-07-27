package com.bridou_n.crossfitsolid.models

import com.google.gson.annotations.SerializedName

/**
 * Created by bridou_n on 27/07/2017.
 */

data class Country(@SerializedName("id") val id: Int?,
                   @SerializedName("name") val name: String?,
                   @SerializedName("alpha2") val aplha2: String?)