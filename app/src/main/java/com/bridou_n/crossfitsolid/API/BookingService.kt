package com.bridou_n.crossfitsolid.API

import com.bridou_n.crossfitsolid.models.Profile
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * Created by bridou_n on 25/07/2017.
 */

interface BookingService {
    @GET("customers/{customerId}")
    fun getProfile(@Path("customerId") customerId: String) : Single<Profile>
}