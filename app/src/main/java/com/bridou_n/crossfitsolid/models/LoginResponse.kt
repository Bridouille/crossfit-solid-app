package com.bridou_n.crossfitsolid.models

import com.google.gson.annotations.SerializedName

/**
 * Created by bridou_n on 25/07/2017.
 */

data class LoginResponse(@SerializedName("username") val username: String,
                         @SerializedName("roles") val roles: Array<String>,
                         @SerializedName("token_type") val token_type: String,
                         @SerializedName("access_token") val access_token: String,
                         @SerializedName("expires_in") val expires_in: Long,
                         @SerializedName("refresh_token") val refresh_token: String)