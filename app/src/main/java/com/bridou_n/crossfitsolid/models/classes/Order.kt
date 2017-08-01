package com.bridou_n.crossfitsolid.models.classes

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

/**
 * Created by bridou_n on 27/07/2017.
 */

data class Order(@SerializedName("id") val id: Int?,
                 @SerializedName("number") val number: Int?) : Parcelable {
    companion object {
        @JvmField val CREATOR: Parcelable.Creator<Order> = object : Parcelable.Creator<Order> {
            override fun createFromParcel(source: Parcel): Order = Order(source)
            override fun newArray(size: Int): Array<Order?> = arrayOfNulls(size)
        }
    }

    constructor(source: Parcel) : this(
    source.readValue(Int::class.java.classLoader) as Int?,
    source.readValue(Int::class.java.classLoader) as Int?
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeValue(id)
        dest.writeValue(number)
    }
}