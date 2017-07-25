package com.bridou_n.crossfitsolid.features.login

/**
 * Created by bridou_n on 25/07/2017.
 */

interface LoginContract {
    interface View {
        fun navigateToMain()
        fun showError(err: String)
    }

    interface Presenter {
        fun start()
        fun stop()

        fun onLoginClicked(username: String, password: String)
    }
}