package com.bridou_n.crossfitsolid.features.login

/**
 * Created by bridou_n on 25/07/2017.
 */

interface LoginContract {
    interface View {
        fun navigateToMain()
        fun showLoading(show: Boolean)
        fun showError(err: Int)
    }

    interface Presenter {
        fun start()
        fun stop()

        fun onLoginClicked(username: String, password: String)
        fun onForgotPassword(email: String)
    }
}