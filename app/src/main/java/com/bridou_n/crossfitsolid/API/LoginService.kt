package com.bridou_n.crossfitsolid.API

import com.bridou_n.crossfitsolid.models.classes.LoginRequest
import com.bridou_n.crossfitsolid.models.classes.LoginResponse
import com.bridou_n.crossfitsolid.models.classes.ResetPasswordRequest
import io.reactivex.Completable
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.POST

/**
 * Created by bridou_n on 27/07/2017.
 */

interface LoginService {
    @POST("auth/login")
    fun login(@Body req: LoginRequest) : Single<LoginResponse>

    @POST("auth/resetpassword")
    fun resetPassword(@Body req: ResetPasswordRequest) : Completable
}