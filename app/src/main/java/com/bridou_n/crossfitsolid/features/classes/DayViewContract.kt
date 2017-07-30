package com.bridou_n.crossfitsolid.features.classes

import com.bridou_n.crossfitsolid.models.GroupActivity
import io.reactivex.Observable

/**
 * Created by bridou_n on 28/07/2017.
 */

interface DayViewContract {
    interface View {
        fun displayClasses(bookings: Array<GroupActivity>)

        fun showLoading(show: Boolean)
        fun showEmptyView()
        fun showError(err: String?) : Observable<Int>
        fun showSmallError(err: String?)
    }

    interface Presenter {
        fun start()
        fun stop()

        fun refresh()
        fun bookClass(groupActivityId: Int, isBooked: Boolean)
    }
}