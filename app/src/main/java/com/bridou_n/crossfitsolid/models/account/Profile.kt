package com.bridou_n.crossfitsolid.models.account

import android.os.Parcel
import android.os.Parcelable
import com.bridou_n.crossfitsolid.models.classes.Address
import com.bridou_n.crossfitsolid.models.classes.Business
import com.bridou_n.crossfitsolid.models.classes.CustomerType
import com.google.gson.annotations.SerializedName

/**
 * Created by bridou_n on 27/07/2017.
 */

data class Profile(@SerializedName("id") val id: Int?,
                   @SerializedName("firstName") val firstName: String?,
                   @SerializedName("lastName") val lastName: String?,
                   @SerializedName("email") val email: String?,
                   @SerializedName("sex") val sex: String?,
                   @SerializedName("ssn") val ssn: String?,
                   @SerializedName("birthDate") val birthDate: String?,
                   @SerializedName("shippingAddress") val shippingAddress: Address?,
                   @SerializedName("billingAddress") val billingAddress: Address?,
                   @SerializedName("mobilePhone") val mobilePhone: String?,
                   @SerializedName("businessUnit") val businessUnit: Business?,
                   @SerializedName("customerType") val customerType: CustomerType?,
                   @SerializedName("customerTypeEndDate") val customerTypeEndDate: String?,
                   @SerializedName("customerNumber") val customerNumber: String?,
                   @SerializedName("cardNumber") val cardNumber: String?,
                   @SerializedName("acceptedBookingTerms") val acceptedBookingTerms: Boolean?,
                   @SerializedName("acceptedSubscriptionTerms") val acceptedSubscriptionTerms: Boolean?,
                   @SerializedName("profileImage") val profileImage: String?) : Parcelable {
    companion object {
        @JvmField val CREATOR: Parcelable.Creator<Profile> = object : Parcelable.Creator<Profile> {
            override fun createFromParcel(source: Parcel): Profile = Profile(source)
            override fun newArray(size: Int): Array<Profile?> = arrayOfNulls(size)
        }
    }

    constructor(source: Parcel) : this(
    source.readValue(Int::class.java.classLoader) as Int?,
    source.readString(),
    source.readString(),
    source.readString(),
    source.readString(),
    source.readString(),
    source.readString(),
    source.readParcelable<Address>(Address::class.java.classLoader),
    source.readParcelable<Address>(Address::class.java.classLoader),
    source.readString(),
    source.readParcelable<Business>(Business::class.java.classLoader),
    source.readParcelable<CustomerType>(CustomerType::class.java.classLoader),
    source.readString(),
    source.readString(),
    source.readString(),
    source.readValue(Boolean::class.java.classLoader) as Boolean?,
    source.readValue(Boolean::class.java.classLoader) as Boolean?,
    source.readString()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeValue(id)
        dest.writeString(firstName)
        dest.writeString(lastName)
        dest.writeString(email)
        dest.writeString(sex)
        dest.writeString(ssn)
        dest.writeString(birthDate)
        dest.writeParcelable(shippingAddress, 0)
        dest.writeParcelable(billingAddress, 0)
        dest.writeString(mobilePhone)
        dest.writeParcelable(businessUnit, 0)
        dest.writeParcelable(customerType, 0)
        dest.writeString(customerTypeEndDate)
        dest.writeString(customerNumber)
        dest.writeString(cardNumber)
        dest.writeValue(acceptedBookingTerms)
        dest.writeValue(acceptedSubscriptionTerms)
        dest.writeString(profileImage)
    }
}