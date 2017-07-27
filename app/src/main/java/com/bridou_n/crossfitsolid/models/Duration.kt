package com.bridou_n.crossfitsolid.models

import com.google.gson.annotations.SerializedName
import java.util.*

/**
 * Created by bridou_n on 27/07/2017.
 */

data class Duration(@SerializedName("start") val start: Date,
                    @SerializedName("end") val end: Date)