package com.bridou_n.crossfitsolid.models.account

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

/**
 * Created by bridou_n on 25/10/2017.
 */

data class MobilePhone(@SerializedName("countryCode") val countryCode: Int?,
                       @SerializedName("number") val number: String?) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readValue(Int::class.java.classLoader) as? Int,
            parcel.readString()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(countryCode)
        parcel.writeString(number)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<MobilePhone> {
        override fun createFromParcel(parcel: Parcel): MobilePhone {
            return MobilePhone(parcel)
        }

        override fun newArray(size: Int): Array<MobilePhone?> {
            return arrayOfNulls(size)
        }
    }
}