package com.bridou_n.crossfitsolid.models.wods

import com.google.gson.annotations.SerializedName

/**
 * Created by bridou_n on 09/02/2018.
 */

class PostPassRequest(@SerializedName("post_password") val postPassword: String,
                      @SerializedName("submit") val submit: String = "oklmzer")