package com.bridou_n.crossfitsolid.API

import com.bridou_n.crossfitsolid.models.wods.PostPassRequest
import com.bridou_n.crossfitsolid.models.wods.Rss
import io.reactivex.Completable
import io.reactivex.Single
import retrofit2.http.*

/**
 * Created by bridou_n on 01/08/2017.
 */

interface WodsService {
    @GET("blogg/wod/feed")
    fun getWods(@Query("paged") pageNb: Int = 0) : Single<Rss>

    @POST("wp-login.php?action=postpass")
    @FormUrlEncoded
    fun postWodsPassword(@FieldMap params: Map<String,String>) : Completable
}