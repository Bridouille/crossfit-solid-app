package com.bridou_n.crossfitsolid.models

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

/**
 * Created by bridou_n on 25/07/2017.
 */

data class LoginRequest(@SerializedName("username") val username: String,
                        @SerializedName("password") val password: String) : Parcelable {
    companion object {
        @JvmField val CREATOR: Parcelable.Creator<LoginRequest> = object : Parcelable.Creator<LoginRequest> {
            override fun createFromParcel(source: Parcel): LoginRequest = LoginRequest(source)
            override fun newArray(size: Int): Array<LoginRequest?> = arrayOfNulls(size)
        }
    }

    constructor(source: Parcel) : this(
    source.readString(),
    source.readString()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(username)
        dest.writeString(password)
    }
}