package com.bridou_n.crossfitsolid.features.classes

import android.util.Log
import com.bridou_n.crossfitsolid.API.BookingService
import com.bridou_n.crossfitsolid.models.BookingError
import com.bridou_n.crossfitsolid.models.BookingRequest
import com.bridou_n.crossfitsolid.models.GroupActivity
import com.bridou_n.crossfitsolid.models.GroupActivityBooking
import com.bridou_n.crossfitsolid.utils.PreferencesManager
import com.bridou_n.crossfitsolid.utils.extensionFunctions.toIso8601Format
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import io.reactivex.BackpressureStrategy
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers
import org.joda.time.LocalDate
import retrofit2.HttpException
import java.io.IOException

/**
 * Created by bridou_n on 28/07/2017.
 */

class DayViewPresenter(val view: DayViewContract.View,
                       val api: BookingService,
                       val gson: Gson,
                       val prefs: PreferencesManager,
                       val currentDate: LocalDate) : DayViewContract.Presenter {

    var disposables: CompositeDisposable = CompositeDisposable()

    override fun start() {
        refresh()
    }

    override fun refresh() {
        // TODO: if it's today, make the start date not starting at start of day
        val start = currentDate.toDateTimeAtStartOfDay().toDate()
        val end = currentDate.plusDays(1).toDateTimeAtStartOfDay().toDate()

        view.showLoading(true)
        disposables.add(Single.zip(
                api.getGroupActivites(startDate = start.toIso8601Format(), endDate = end.toIso8601Format()),
                api.getMyGroupActivities(prefs.getUserId() ?: ""),
                BiFunction { allActivites: Array<GroupActivity>, myActivities: Array<GroupActivityBooking> ->

                    // Modify the list of all activites so we know wich ones are booked by us
                    for (ga in allActivites) {
                        val activityId = ga.id

                        myActivities
                                .filter { activityId == it.groupActivity?.id }
                                .forEach {
                                    ga.slots?.isBooked = true
                                    // We store the id of the activityBooking in case we need to DELETE it
                                    ga.groupActivityProduct?.id = it.groupActivityBooking?.id
                                }
                    }
                    allActivites
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .retryWhen { errors ->
                    errors.flatMap {
                        error: Throwable ->

                        view.showError(getErrorMessage(error))
                            .toFlowable(BackpressureStrategy.LATEST)
                            .doOnNext { _ -> view.showLoading(true) }
                    }
                }
                .subscribe({ results ->
                    view.showLoading(false)

                    if (results.isEmpty()) {
                        view.showEmptyView()
                    } else {
                        view.displayClasses(results)
                    }
                }))
    }

    override fun stop() {
        disposables.clear()
    }

    override fun bookClass(groupActivityId: Int, isBooked: Boolean) {
        // TODO show popup to ask confirmation of the booking
        Log.d("DayViewPresenter", "book class : $groupActivityId, isBooking: $isBooked")

        val req = if (isBooked) {
            api.cancelBooking(prefs.getUserId() ?: "", groupActivityId, id = GroupActivity(groupActivityId))
        } else {
            api.bookActivity(prefs.getUserId() ?: "", BookingRequest(groupActivityId))
        }

        disposables.add(req
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    refresh()
                }, { err ->
                    err.printStackTrace()

                    view.showSmallError(getErrorMessage(err))
                }))

    }

    fun getErrorMessage(err: Throwable) : String? {
        return when (err) {
            is HttpException -> {
                val body = err.response().errorBody()

                try {
                    val error = gson.fromJson(body?.string(), BookingError::class.java)

                    error?.errorCode ?: err.message()
                } catch (e: JsonSyntaxException) {
                    err.message
                }
            }
            is IOException -> {
                null
            }
            else -> {
                err.message
            }
        }
    }
}