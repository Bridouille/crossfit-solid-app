package com.bridou_n.crossfitsolid.API

import com.bridou_n.crossfitsolid.models.account.Profile
import com.bridou_n.crossfitsolid.models.classes.BookingRequest
import com.bridou_n.crossfitsolid.models.classes.GroupActivity
import com.bridou_n.crossfitsolid.models.classes.GroupActivityBooking
import io.reactivex.Completable
import io.reactivex.Single
import retrofit2.http.*

/**
 * Created by bridou_n on 25/07/2017.
 */

interface BookingService {
    @GET("customers/{customerId}")
    fun getProfile(@Path("customerId") customerId: String) : Single<Profile>

    @GET("businessunits/{businessUnitId}/groupactivities")
    fun getGroupActivites(@Path("businessUnitId") businessUnitId: Int = 1,
                          @Query("period.start") startDate: String,
                          @Query("period.stop") endDate: String,
                          @Query("webCategory") webCategory: Int = 1) : Single<Array<GroupActivity>>

    @GET("customers/{customerId}/bookings/groupactivities")
    fun getMyGroupActivities(@Path("customerId") customerId: String) : Single<Array<GroupActivityBooking>>

    @POST("customers/{customerId}/bookings/groupactivities")
    fun bookActivity(@Path("customerId") customerId: String,
                     @Body activity: BookingRequest) : Completable

    @HTTP(method = "DELETE", path = "customers/{customerId}/bookings/groupactivities/{activityId}", hasBody = true)
    fun cancelBooking(@Path("customerId") customerId: String,
                      @Path("activityId") activityId: Int,
                      @Query("bookingType") bookingType: String = "groupActivityBooking",
                      @Body id: GroupActivity) : Completable
}