package com.bridou_n.crossfitsolid.features.login

import com.bridou_n.crossfitsolid.API.BookingService
import com.bridou_n.crossfitsolid.models.LoginRequest
import com.bridou_n.crossfitsolid.utils.PreferencesManager
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

/**
 * Created by bridou_n on 25/07/2017.
 */

class LoginPresenter(val view: LoginContract.View,
                     val api: BookingService,
                     val prefs: PreferencesManager) : LoginContract.Presenter {

    var disposable: Disposable? = null

    override fun start() {

    }

    override fun stop() {
        disposable?.dispose()
    }

    override fun onLoginClicked(username: String, password: String) {
        if (username.isEmpty() || password.isEmpty()) {
            // TODO: localize
            view.showError("The username or password is empty")
            return
        }

        disposable = api.login(LoginRequest(username, password))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ resp ->
                    // Store the username and password in the sharedPreferences
                    prefs.setUsername(username)
                    prefs.setPassword(password)

                    // Redirect to the logged part
                    view.navigateToMain()
                }, { err ->
                    // TODO: localize
                    view.showError("Wrong username or password")
                })
    }
}