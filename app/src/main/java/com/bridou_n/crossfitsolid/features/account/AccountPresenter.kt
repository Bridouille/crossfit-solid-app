package com.bridou_n.crossfitsolid.features.account

import android.util.Log
import com.bridou_n.crossfitsolid.API.BookingService
import com.bridou_n.crossfitsolid.models.account.Profile
import com.bridou_n.crossfitsolid.utils.BasePresenter
import com.bridou_n.crossfitsolid.utils.PreferencesManager
import com.google.gson.Gson
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Function
import io.reactivex.schedulers.Schedulers
import java.util.*

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
        disposable = Observable.concat(
                    prefs.getProfile().toObservable(),
                    api.getProfile(prefs.getUserId() ?: "").toObservable()
                            .doOnNext {
                                // Store locally the profile received from the network
                                profile -> prefs.setProfile(profile)
                            }
                            .onErrorResumeNext{ error: Throwable ->
                                // If we have a network error but we have a cached profile, ignore it
                                val prof = prefs.getProfile().toObservable().blockingFirst(null)

                                if (prof == null) Observable.error { error } else Observable.empty()
                            })
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