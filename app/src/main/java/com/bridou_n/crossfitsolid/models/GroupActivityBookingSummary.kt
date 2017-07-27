package com.bridou_n.crossfitsolid.models

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

/**
 * Created by bridou_n on 27/07/2017.
 */

data class GroupActivityBookingSummary(@SerializedName("id") val id: Int?,
                                       @SerializedName("order") val order: Order?) : Parcelable {
    companion object {
        @JvmField val CREATOR: Parcelable.Creator<GroupActivityBookingSummary> = object : Parcelable.Creator<GroupActivityBookingSummary> {
            override fun createFromParcel(source: Parcel): GroupActivityBookingSummary = GroupActivityBookingSummary(source)
            override fun newArray(size: Int): Array<GroupActivityBookingSummary?> = arrayOfNulls(size)
        }
    }

    constructor(source: Parcel) : this(
    source.readValue(Int::class.java.classLoader) as Int?,
    source.readParcelable<Order>(Order::class.java.classLoader)
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeValue(id)
        dest.writeParcelable(order, 0)
    }
}