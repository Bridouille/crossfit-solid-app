package com.bridou_n.crossfitsolid.models.classes

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import java.util.*

/**
 * Created by bridou_n on 27/07/2017.
 */

data class Duration(@SerializedName("start") val start: Date?,
                    @SerializedName("end") val end: Date?) : Parcelable {
    companion object {
        @JvmField val CREATOR: Parcelable.Creator<Duration> = object : Parcelable.Creator<Duration> {
            override fun createFromParcel(source: Parcel): Duration = Duration(source)
            override fun newArray(size: Int): Array<Duration?> = arrayOfNulls(size)
        }
    }

    constructor(source: Parcel) : this(
    source.readSerializable() as Date?,
    source.readSerializable() as Date?
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeSerializable(start)
        dest.writeSerializable(end)
    }
}