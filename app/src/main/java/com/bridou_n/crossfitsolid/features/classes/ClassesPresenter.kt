package com.bridou_n.crossfitsolid.features.classes

import android.util.Log
import com.bridou_n.crossfitsolid.API.BookingService
import com.bridou_n.crossfitsolid.models.GroupActivity
import com.bridou_n.crossfitsolid.models.GroupActivityBooking
import com.bridou_n.crossfitsolid.utils.PreferencesManager
import com.bridou_n.crossfitsolid.utils.extensionFunctions.toIso8601Format
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers
import java.util.*

/**
 * Created by bridou_n on 27/07/2017.
 */

class ClassesPresenter(val view: ClassesContract.View,
                       val api: BookingService,
                       val prefs: PreferencesManager) : ClassesContract.Presenter {

    var disposable: Disposable? = null

    override fun start() {
        refresh()
    }

    override fun refresh() {
        val start = Date()
        val end = Date(start.time + 7 * 24 * 60 * 60 * 1000)

        view.showLoading(true)
        disposable = Single.zip(
                api.getGroupActivites(startDate = start.toIso8601Format(), endDate = end.toIso8601Format()),
                api.getMyGroupActivities(prefs.getUserId() ?: ""),
                BiFunction { allActivites: Array<GroupActivity>, myActivities: Array<GroupActivityBooking> ->

                    // Modify the list of all activites so we know wich ones are booked by us
                    for (ga in allActivites) {
                        val activityId = ga.id

                        Log.d("ClassesPresenter", "ActId: $activityId")
                        for (booked in myActivities) {
                            if (activityId == booked.groupActivity?.id) {
                                Log.d("ClassesPresenter", "Found one booked class : ${ga.name}")
                                ga.slots?.isBooked = true
                            }
                        }
                    }

                    allActivites
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ results ->
                    view.showLoading(false)
                    view.displayClasses(results)
                }, { err ->
                    view.showLoading(false)
                    view.showError(err.message)
                })
    }

    override fun stop() {
        disposable?.dispose()
    }

    override fun bookClass() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun unbookClass() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}