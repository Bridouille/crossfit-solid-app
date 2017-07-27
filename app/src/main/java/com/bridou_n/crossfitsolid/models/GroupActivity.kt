package com.bridou_n.crossfitsolid.models

import com.google.gson.annotations.SerializedName
import java.util.*

/**
 * Created by bridou_n on 27/07/2017.
 */

data class GroupActivity(@SerializedName("id") val id: Int?,
                         @SerializedName("name") val name: String?,
                         @SerializedName("duration") val duration: Duration?,
                         @SerializedName("groupActivityProduct") val groupActivityProduct: GroupActivityProduct?,
                         @SerializedName("businessUnit") val business: Business?,
                         @SerializedName("locations") val locations: Array<Location>?,
                         @SerializedName("insctructors") val instructos: Array<Instructor>?,
                         @SerializedName("bookableEarliest") val bookableEarliest: Date?,
                         @SerializedName("bookableLatest") val bookableLatest: Date?,
                         @SerializedName("externalMessage") val externalMessage: String?,
                         @SerializedName("cancelled") val cancelled: Boolean?,
                         @SerializedName("slots") val slots: Slots?)