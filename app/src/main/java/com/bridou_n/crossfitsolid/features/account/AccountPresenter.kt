package com.bridou_n.crossfitsolid.features.account

import com.bridou_n.crossfitsolid.API.BookingService
import com.bridou_n.crossfitsolid.utils.PreferencesManager
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

/**
 * Created by bridou_n on 30/07/2017.
 */

class AccountPresenter(val view: AccountContract.View,
                       val api: BookingService,
                       val prefs: PreferencesManager) : AccountContract.Presenter {

    var disposable: Disposable? = null

    override fun start() {
        disposable = api.getProfile(prefs.getUserId() ?: "")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ profile ->
                    view.showProfile(profile)
                }, { err ->
                    err.printStackTrace()
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