package com.bridou_n.crossfitsolid.features.classes

import android.text.format.DateUtils
import com.bridou_n.crossfitsolid.API.BookingService
import com.bridou_n.crossfitsolid.models.BookingError
import com.bridou_n.crossfitsolid.models.BookingRequest
import com.bridou_n.crossfitsolid.models.GroupActivity
import com.bridou_n.crossfitsolid.models.GroupActivityBooking
import com.bridou_n.crossfitsolid.utils.BasePresenter
import com.bridou_n.crossfitsolid.utils.PreferencesManager
import com.bridou_n.crossfitsolid.utils.extensionFunctions.isToday
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
import java.util.*

/**
 * Created by bridou_n on 28/07/2017.
 */

class DayViewPresenter(val view: DayViewContract.View,
                       val api: BookingService,
                       val gson: Gson,
                       val prefs: PreferencesManager,
                       val currentDate: LocalDate) : DayViewContract.Presenter, BasePresenter() {

    var disposables: CompositeDisposable = CompositeDisposable()

    override fun start() {
        refresh()
    }

    override fun refresh() {
        val start = if (currentDate.isToday()) Date(Date().time - (DateUtils.HOUR_IN_MILLIS * 2)) else currentDate.toDateTimeAtStartOfDay().toDate()
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

                        view.showError(getErrorMessage(error, gson))
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

                    view.showSmallError(getErrorMessage(err, gson))
                }))

    }
}