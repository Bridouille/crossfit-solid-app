package com.bridou_n.crossfitsolid.models.classes

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

/**
 * Created by bridou_n on 27/07/2017.
 */

data class Slots(@SerializedName("total") val total: Int?,
                 @SerializedName("totalBookable") val totalBookable: Int?,
                 @SerializedName("reservedForDropin") val reservedForDropin: Int?,
                 @SerializedName("leftToBook") val leftToBook: Int?,
                 @SerializedName("hasWaitingList") val hasWaitingList: Boolean?,
                 @SerializedName("inWaitingList") val inWaitingList: Int?) : Parcelable {
    companion object {
        @JvmField val CREATOR: Parcelable.Creator<Slots> = object : Parcelable.Creator<Slots> {
            override fun createFromParcel(source: Parcel): Slots = Slots(source)
            override fun newArray(size: Int): Array<Slots?> = arrayOfNulls(size)
        }
    }

    constructor(source: Parcel) : this(
    source.readValue(Int::class.java.classLoader) as Int?,
    source.readValue(Int::class.java.classLoader) as Int?,
    source.readValue(Int::class.java.classLoader) as Int?,
    source.readValue(Int::class.java.classLoader) as Int?,
    source.readValue(Boolean::class.java.classLoader) as Boolean?,
    source.readValue(Int::class.java.classLoader) as Int?
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeValue(total)
        dest.writeValue(totalBookable)
        dest.writeValue(reservedForDropin)
        dest.writeValue(leftToBook)
        dest.writeValue(hasWaitingList)
        dest.writeValue(inWaitingList)
    }
}