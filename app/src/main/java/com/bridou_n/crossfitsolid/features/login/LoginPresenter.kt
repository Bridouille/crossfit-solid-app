package com.bridou_n.crossfitsolid.features.login

import com.bridou_n.crossfitsolid.API.BookingService
import com.bridou_n.crossfitsolid.API.LoginService
import com.bridou_n.crossfitsolid.R
import com.bridou_n.crossfitsolid.models.LoginRequest
import com.bridou_n.crossfitsolid.utils.PreferencesManager
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

/**
 * Created by bridou_n on 25/07/2017.
 */

class LoginPresenter(val view: LoginContract.View,
                     val api: LoginService,
                     val prefs: PreferencesManager) : LoginContract.Presenter {

    var disposable: Disposable? = null

    override fun start() {

    }

    override fun stop() {
        disposable?.dispose()
    }

    override fun onLoginClicked(username: String, password: String) {
        if (username.isEmpty() || password.isEmpty()) {
            view.showError(R.string.username_or_password_empty)
            return
        }

        view.showLoading(true)

        disposable?.dispose() // Cancel previous requests if any..
        disposable = api.login(LoginRequest(username, password))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ resp ->
                    view.showLoading(false)

                    // Store the username / password & in the sharedPreferences
                    prefs.setUsername(username)
                    prefs.setPassword(password)

                    // Redirect to the logged part
                    view.navigateToMain()
                }, { err ->
                    view.showLoading(false)

                    view.showError(R.string.wrong_username_or_password)
                })
    }
}