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
                   @SerializedName("mobilePhone") val mobilePhone: MobilePhone?,
                   @SerializedName("businessUnit") val businessUnit: Business?,
                   @SerializedName("customerType") val customerType: CustomerType?,
                   @SerializedName("customerTypeEndDate") val customerTypeEndDate: String?,
                   @SerializedName("customerNumber") val customerNumber: String?,
                   @SerializedName("cardNumber") val cardNumber: String?,
                   @SerializedName("acceptedBookingTerms") val acceptedBookingTerms: Boolean?,
                   @SerializedName("acceptedSubscriptionTerms") val acceptedSubscriptionTerms: Boolean?,
                   @SerializedName("profileImage") val profileImage: String?) : Parcelable {
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
            source.readParcelable<MobilePhone>(MobilePhone::class.java.classLoader),
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

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeValue(id)
        writeString(firstName)
        writeString(lastName)
        writeString(email)
        writeString(sex)
        writeString(ssn)
        writeString(birthDate)
        writeParcelable(shippingAddress, 0)
        writeParcelable(billingAddress, 0)
        writeParcelable(mobilePhone, 0)
        writeParcelable(businessUnit, 0)
        writeParcelable(customerType, 0)
        writeString(customerTypeEndDate)
        writeString(customerNumber)
        writeString(cardNumber)
        writeValue(acceptedBookingTerms)
        writeValue(acceptedSubscriptionTerms)
        writeString(profileImage)
    }

    companion object {
        @JvmField val CREATOR: Parcelable.Creator<Profile> = object : Parcelable.Creator<Profile> {
            override fun createFromParcel(source: Parcel): Profile = Profile(source)
            override fun newArray(size: Int): Array<Profile?> = arrayOfNulls(size)
        }
    }
}