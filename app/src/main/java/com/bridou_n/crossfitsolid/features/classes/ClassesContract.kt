package com.bridou_n.crossfitsolid.features.classes

import com.bridou_n.crossfitsolid.models.GroupActivity

/**
 * Created by bridou_n on 27/07/2017.
 */

interface ClassesContract {
    interface View {
        fun displayClasses(bookings: Array<GroupActivity>)

        fun showLoading(show: Boolean)
        fun showError(err: String?)
    }

    interface Presenter {
        fun start()
        fun stop()

        fun refresh()
        fun bookClass()
        fun unbookClass()
    }
}