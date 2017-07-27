package com.bridou_n.crossfitsolid.models

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

/**
 * Created by bridou_n on 27/07/2017.
 */

data class Instructor(@SerializedName("id") val id: Int?,
                      @SerializedName("name") val name: String?,
                      @SerializedName("isSubstitute") val isSubstitute: Boolean?) : Parcelable {
    companion object {
        @JvmField val CREATOR: Parcelable.Creator<Instructor> = object : Parcelable.Creator<Instructor> {
            override fun createFromParcel(source: Parcel): Instructor = Instructor(source)
            override fun newArray(size: Int): Array<Instructor?> = arrayOfNulls(size)
        }
    }

    constructor(source: Parcel) : this(
    source.readValue(Int::class.java.classLoader) as Int?,
    source.readString(),
    source.readValue(Boolean::class.java.classLoader) as Boolean?
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeValue(id)
        dest.writeString(name)
        dest.writeValue(isSubstitute)
    }
}