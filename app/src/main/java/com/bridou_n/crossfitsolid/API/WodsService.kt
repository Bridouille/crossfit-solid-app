package com.bridou_n.crossfitsolid.API

import com.bridou_n.crossfitsolid.models.wods.Rss
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by bridou_n on 01/08/2017.
 */

interface WodsService {
    @GET("blogg/wod/feed")
    fun getWods(@Query("paged") pageNb: Int = 0) : Single<Rss>
}