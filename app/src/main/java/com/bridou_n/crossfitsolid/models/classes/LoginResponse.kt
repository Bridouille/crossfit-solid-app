package com.bridou_n.crossfitsolid.models.classes

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

/**
 * Created by bridou_n on 25/07/2017.
 */

data class LoginResponse(@SerializedName("username") val username: String,
                         @SerializedName("roles") val roles: Array<String>,
                         @SerializedName("token_type") val token_type: String,
                         @SerializedName("access_token") val access_token: String,
                         @SerializedName("expires_in") val expires_in: Long,
                         @SerializedName("refresh_token") val refresh_token: String) : Parcelable {
    companion object {
        @JvmField val CREATOR: Parcelable.Creator<LoginResponse> = object : Parcelable.Creator<LoginResponse> {
            override fun createFromParcel(source: Parcel): LoginResponse = LoginResponse(source)
            override fun newArray(size: Int): Array<LoginResponse?> = arrayOfNulls(size)
        }
    }

    constructor(source: Parcel) : this(
    source.readString(),
    source.createStringArray(),
    source.readString(),
    source.readString(),
    source.readLong(),
    source.readString()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(username)
        dest.writeStringArray(roles)
        dest.writeString(token_type)
        dest.writeString(access_token)
        dest.writeLong(expires_in)
        dest.writeString(refresh_token)
    }
}