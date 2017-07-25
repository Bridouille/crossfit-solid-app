package com.bridou_n.crossfitsolid.models

import com.google.gson.annotations.SerializedName

/**
 * Created by bridou_n on 25/07/2017.
 */

data class LoginRequest(@SerializedName("username") val username: String,
                        @SerializedName("password") val password: String)