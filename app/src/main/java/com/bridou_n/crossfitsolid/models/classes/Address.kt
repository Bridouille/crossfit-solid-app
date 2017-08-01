package com.bridou_n.crossfitsolid.models.classes

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

/**
 * Created by bridou_n on 27/07/2017.
 */

data class Address(@SerializedName("postalCode") val postalCode: String?,
                   @SerializedName("city") val city: String?,
                   @SerializedName("street") val street: String?,
                   @SerializedName("careOf") val careOf: String?,
                   @SerializedName("country") val country: Country?) : Parcelable {
    companion object {
        @JvmField val CREATOR: Parcelable.Creator<Address> = object : Parcelable.Creator<Address> {
            override fun createFromParcel(source: Parcel): Address = Address(source)
            override fun newArray(size: Int): Array<Address?> = arrayOfNulls(size)
        }
    }

    constructor(source: Parcel) : this(
    source.readString(),
    source.readString(),
    source.readString(),
    source.readString(),
    source.readParcelable<Country>(Country::class.java.classLoader)
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(postalCode)
        dest.writeString(city)
        dest.writeString(street)
        dest.writeString(careOf)
        dest.writeParcelable(country, 0)
    }
}