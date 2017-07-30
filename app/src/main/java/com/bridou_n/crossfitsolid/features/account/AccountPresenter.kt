package com.bridou_n.crossfitsolid.features.account

import com.bridou_n.crossfitsolid.API.BookingService
import com.bridou_n.crossfitsolid.utils.BasePresenter
import com.bridou_n.crossfitsolid.utils.PreferencesManager
import com.google.gson.Gson
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

/**
 * Created by bridou_n on 30/07/2017.
 */

class AccountPresenter(val view: AccountContract.View,
                       val api: BookingService,
                       val prefs: PreferencesManager,
                       val gson: Gson) : AccountContract.Presenter, BasePresenter() {

    var disposable: Disposable? = null

    override fun start() {
        refresh()
    }

    override fun refresh() {
        disposable?.dispose()
        disposable = api.getProfile(prefs.getUserId() ?: "")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ profile ->
                    view.showProfile(profile)
                }, { err ->
                    view.showError(getErrorMessage(err, gson))
                })
    }

    override fun stop() {
        disposable?.dispose()
    }

    override fun logout() {
        prefs.clear()
        view.logoutRedirect()
    }
}