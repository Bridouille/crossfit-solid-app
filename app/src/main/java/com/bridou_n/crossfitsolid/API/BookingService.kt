package com.bridou_n.crossfitsolid.API

import com.bridou_n.crossfitsolid.models.LoginRequest
import com.bridou_n.crossfitsolid.models.LoginResponse
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.POST

/**
 * Created by bridou_n on 25/07/2017.
 */

interface BookingService {
    @POST("auth/login")
    fun login(@Body req: LoginRequest) : Single<LoginResponse>
}