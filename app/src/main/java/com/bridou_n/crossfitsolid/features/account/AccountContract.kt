package com.bridou_n.crossfitsolid.features.account

import com.bridou_n.crossfitsolid.models.account.Profile

/**
 * Created by bridou_n on 30/07/2017.
 */

interface AccountContract {
    interface View {
        fun showProfile(profile: Profile)
        fun showError(err: String?)

        fun logoutRedirect()
    }

    interface Presenter {
        fun start()
        fun stop()

        fun refresh()
        fun logout()
    }
}