package com.bridou_n.crossfitsolid.features.wods

/**
 * Created by bridou_n on 01/08/2017.
 */

interface WodsContract {
    interface View {
        fun showLastUpdate(lastUpdateTime: Long)
        fun scrollToTop()

        fun showLoading(show: Boolean)
        fun showEmptyState(show: Boolean)
        fun showError(err: String?, paged: Int)
    }

    interface Presenter {
        fun start()
        fun stop()

        fun refresh(paged: Int = 0)
    }
}