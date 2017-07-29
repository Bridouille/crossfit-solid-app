package com.bridou_n.crossfitsolid.features.classes

import android.util.Log
import com.bridou_n.crossfitsolid.API.BookingService
import com.bridou_n.crossfitsolid.models.GroupActivity
import com.bridou_n.crossfitsolid.models.GroupActivityBooking
import com.bridou_n.crossfitsolid.utils.PreferencesManager
import com.bridou_n.crossfitsolid.utils.extensionFunctions.toIso8601Format
import io.reactivex.BackpressureStrategy
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers
import org.joda.time.LocalDate

/**
 * Created by bridou_n on 28/07/2017.
 */

class DayViewPresenter(val view: DayViewContract.View,
                       val api: BookingService,
                       val prefs: PreferencesManager,
                       val currentDate: LocalDate) : DayViewContract.Presenter {

    var disposable: Disposable? = null

    override fun start() {
        refresh()
    }

    // TODO: to test the error on booking classes, allow old dates to get a failure from api
    override fun refresh() {
        // TODO: if it's today, make the start date not starting at start of day
        val start = currentDate.toDateTimeAtStartOfDay().toDate()
        val end = currentDate.plusDays(1).toDateTimeAtStartOfDay().toDate()

        view.showLoading(true)
        disposable = Single.zip(
                api.getGroupActivites(startDate = start.toIso8601Format(), endDate = end.toIso8601Format()),
                api.getMyGroupActivities(prefs.getUserId() ?: ""),
                BiFunction { allActivites: Array<GroupActivity>, myActivities: Array<GroupActivityBooking> ->

                    // Modify the list of all activites so we know wich ones are booked by us
                    for (ga in allActivites) {
                        val activityId = ga.id

                        myActivities
                                .filter { activityId == it.groupActivity?.id }
                                .forEach { ga.slots?.isBooked = true }
                    }
                    allActivites
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .retryWhen { errors ->
                    errors.flatMap {
                        error: Throwable ->

                        view.showError(error.message)
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
                })
    }

    override fun stop() {
        disposable?.dispose()
    }

    override fun bookClass(groupActivityId: Int, isBooking: Boolean) {
        // TODO show popup to ask confirmation of the booking
        Log.d("DayViewPresenter", "book class : $groupActivityId, isBooking: $isBooking")
    }
}