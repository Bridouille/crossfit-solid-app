package com.bridou_n.crossfitsolid.models.classes

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

/**
 * Created by bridou_n on 30/07/2017.
 */

data class WaitingListBooking(@SerializedName("id") val id: Int?,
                              @SerializedName("waitingListPosition") val waitingListPosition: Int?) : Parcelable {
    companion object {
        @JvmField val CREATOR: Parcelable.Creator<WaitingListBooking> = object : Parcelable.Creator<WaitingListBooking> {
            override fun createFromParcel(source: Parcel): WaitingListBooking = WaitingListBooking(source)
            override fun newArray(size: Int): Array<WaitingListBooking?> = arrayOfNulls(size)
        }
    }

    constructor(source: Parcel) : this(
    source.readValue(Int::class.java.classLoader) as Int?,
    source.readValue(Int::class.java.classLoader) as Int?
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeValue(id)
        dest.writeValue(waitingListPosition)
    }
}