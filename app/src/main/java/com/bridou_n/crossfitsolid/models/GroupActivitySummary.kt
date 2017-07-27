package com.bridou_n.crossfitsolid.models

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

/**
 * Created by bridou_n on 27/07/2017.
 */

data class GroupActivitySummary(@SerializedName("id") val id: Int?,
                                @SerializedName("name") val name: String?) : Parcelable {
    companion object {
        @JvmField val CREATOR: Parcelable.Creator<GroupActivitySummary> = object : Parcelable.Creator<GroupActivitySummary> {
            override fun createFromParcel(source: Parcel): GroupActivitySummary = GroupActivitySummary(source)
            override fun newArray(size: Int): Array<GroupActivitySummary?> = arrayOfNulls(size)
        }
    }

    constructor(source: Parcel) : this(
    source.readValue(Int::class.java.classLoader) as Int?,
    source.readString()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeValue(id)
        dest.writeString(name)
    }
}