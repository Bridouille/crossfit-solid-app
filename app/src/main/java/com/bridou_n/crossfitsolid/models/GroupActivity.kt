package com.bridou_n.crossfitsolid.models

import android.os.Parcel
import android.os.Parcelable
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
                         @SerializedName("instructors") val instructors: Array<Instructor>?,
                         @SerializedName("bookableEarliest") val bookableEarliest: Date?,
                         @SerializedName("bookableLatest") val bookableLatest: Date?,
                         @SerializedName("externalMessage") val externalMessage: String?,
                         @SerializedName("cancelled") val cancelled: Boolean?,
                         @SerializedName("slots") val slots: Slots?) : Parcelable {
    companion object {
        @JvmField val CREATOR: Parcelable.Creator<GroupActivity> = object : Parcelable.Creator<GroupActivity> {
            override fun createFromParcel(source: Parcel): GroupActivity = GroupActivity(source)
            override fun newArray(size: Int): Array<GroupActivity?> = arrayOfNulls(size)
        }
    }

    constructor(source: Parcel) : this(
    source.readValue(Int::class.java.classLoader) as Int?,
    source.readString(),
    source.readParcelable<Duration>(Duration::class.java.classLoader),
    source.readParcelable<GroupActivityProduct>(GroupActivityProduct::class.java.classLoader),
    source.readParcelable<Business>(Business::class.java.classLoader),
    source.readParcelableArray(Location::class.java.classLoader) as Array<Location>?,
    source.readParcelableArray(Instructor::class.java.classLoader) as Array<Instructor>?,
    source.readSerializable() as Date?,
    source.readSerializable() as Date?,
    source.readString(),
    source.readValue(Boolean::class.java.classLoader) as Boolean?,
    source.readParcelable<Slots>(Slots::class.java.classLoader)
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeValue(id)
        dest.writeString(name)
        dest.writeParcelable(duration, 0)
        dest.writeParcelable(groupActivityProduct, 0)
        dest.writeParcelable(business, 0)
        dest.writeParcelableArray(locations, 0)
        dest.writeParcelableArray(instructors, 0)
        dest.writeSerializable(bookableEarliest)
        dest.writeSerializable(bookableLatest)
        dest.writeString(externalMessage)
        dest.writeValue(cancelled)
        dest.writeParcelable(slots, 0)
    }
}