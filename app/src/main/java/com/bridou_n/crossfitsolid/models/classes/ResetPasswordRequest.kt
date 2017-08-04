package com.bridou_n.crossfitsolid.models.classes

import com.google.gson.annotations.SerializedName

/**
 * Created by bridou_n on 04/08/2017.
 */

data class ResetPasswordRequest(@SerializedName("email") val email: String?)