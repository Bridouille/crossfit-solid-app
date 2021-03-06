package com.bridou_n.crossfitsolid.utils

import com.bridou_n.crossfitsolid.models.classes.BookingError
import com.google.firebase.crash.FirebaseCrash
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import retrofit2.HttpException
import java.io.IOException

/**
 * Created by bridou_n on 30/07/2017.
 */

open class BasePresenter() {

    fun getErrorMessage(err: Throwable, gson: Gson) : String? {
        return when (err) {
            is HttpException -> {
                val body = err.response().errorBody()

                try {
                    val error = gson.fromJson(body?.string(), BookingError::class.java)

                    error.errorCode ?: err.message()
                } catch (e: Throwable) {
                    err.message
                }
            }
            is IOException -> {
                null
            }
            else -> {
                FirebaseCrash.report(err)
                err.message
            }
        }
    }
}