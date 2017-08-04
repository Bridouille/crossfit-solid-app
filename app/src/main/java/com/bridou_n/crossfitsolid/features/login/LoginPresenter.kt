package com.bridou_n.crossfitsolid.features.login

import com.bridou_n.crossfitsolid.API.LoginService
import com.bridou_n.crossfitsolid.R
import com.bridou_n.crossfitsolid.models.classes.LoginRequest
import com.bridou_n.crossfitsolid.models.classes.ResetPasswordRequest
import com.bridou_n.crossfitsolid.utils.PreferencesManager
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

/**
 * Created by bridou_n on 25/07/2017.
 */

class LoginPresenter(val view: LoginContract.View,
                     val api: LoginService,
                     val prefs: PreferencesManager) : LoginContract.Presenter {

    var disposables: CompositeDisposable = CompositeDisposable()

    override fun start() {

    }

    override fun stop() {
        disposables.clear()
    }

    override fun onLoginClicked(username: String, password: String) {
        if (username.isEmpty() || password.isEmpty()) {
            return view.showError(R.string.username_or_password_empty)
        }

        view.showLoading(true)

        disposables.clear() // Cancel previous requests if any..
        disposables.add(api.login(LoginRequest(username, password))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ resp ->
                    view.showLoading(false)

                    // Store the username / password & in the sharedPreferences
                    prefs.setUsername(username)
                    prefs.setPassword(password)

                    prefs.setToken(resp.access_token)
                    prefs.setUserId(resp.username)

                    // Redirect to the logged part
                    view.navigateToMain()
                }, { _ ->
                    view.showLoading(false)

                    view.showError(R.string.wrong_username_or_password)
                }))
    }

    override fun onForgotPassword(email: String) {
        if (email.isEmpty() || !email.contains("@")) {
            return view.showError(R.string.email_not_valid)
        }

        view.showLoading(true)
        disposables.add(api.resetPassword(ResetPasswordRequest(email))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    view.showLoading(false)
                    view.showError(R.string.instructions_have_been_sent)
                }, { err: Throwable ->
                    err.printStackTrace()
                    view.showLoading(false)
                    view.showError(R.string.email_not_valid)
                }))
    }
}