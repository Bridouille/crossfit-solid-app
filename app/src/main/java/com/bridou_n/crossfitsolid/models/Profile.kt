package com.bridou_n.crossfitsolid.models

import com.google.gson.annotations.SerializedName

/**
 * Created by bridou_n on 27/07/2017.
 */

data class Profile(@SerializedName("id") val id: Int?,
                   @SerializedName("firstName") val firstName: String?,
                   @SerializedName("lastName") val lastName: String?,
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
                   @SerializedName("profileImage") val profileImage: String?)