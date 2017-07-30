package com.bridou_n.crossfitsolid.models

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

/**
 * Created by bridou_n on 27/07/2017.
 */

data class GroupActivityBooking(@SerializedName("type") val type: String?,
                                @SerializedName("groupActivity") val groupActivity: GroupActivitySummary?,
                                @SerializedName("businessUnit") val businessUnit: Business?,
                                @SerializedName("customer") val customer: Profile?,
                                @SerializedName("duration") val duration: Duration?,
                                @SerializedName("groupActivityBooking") val groupActivityBooking: GroupActivityBookingSummary?,
                                @SerializedName("waitingListBooking") val waitingListBooking: WaitingListBooking?,
                                @SerializedName("additionToEventBooking") val additionToEventBooking: String?) : Parcelable {
    companion object {
        @JvmField val CREATOR: Parcelable.Creator<GroupActivityBooking> = object : Parcelable.Creator<GroupActivityBooking> {
            override fun createFromParcel(source: Parcel): GroupActivityBooking = GroupActivityBooking(source)
            override fun newArray(size: Int): Array<GroupActivityBooking?> = arrayOfNulls(size)
        }
    }

    constructor(source: Parcel) : this(
    source.readString(),
    source.readParcelable<GroupActivitySummary>(GroupActivitySummary::class.java.classLoader),
    source.readParcelable<Business>(Business::class.java.classLoader),
    source.readParcelable<Profile>(Profile::class.java.classLoader),
    source.readParcelable<Duration>(Duration::class.java.classLoader),
    source.readParcelable<GroupActivityBookingSummary>(GroupActivityBookingSummary::class.java.classLoader),
    source.readParcelable<WaitingListBooking>(WaitingListBooking::class.java.classLoader),
    source.readString()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(type)
        dest.writeParcelable(groupActivity, 0)
        dest.writeParcelable(businessUnit, 0)
        dest.writeParcelable(customer, 0)
        dest.writeParcelable(duration, 0)
        dest.writeParcelable(groupActivityBooking, 0)
        dest.writeParcelable(waitingListBooking, 0)
        dest.writeString(additionToEventBooking)
    }
}