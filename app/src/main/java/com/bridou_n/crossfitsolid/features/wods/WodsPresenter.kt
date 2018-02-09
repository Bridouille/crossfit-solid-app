package com.bridou_n.crossfitsolid.features.wods

import android.text.format.DateUtils
import android.util.Log
import com.bridou_n.crossfitsolid.API.WodsService
import com.bridou_n.crossfitsolid.models.wods.PostPassRequest
import com.bridou_n.crossfitsolid.utils.BasePresenter
import com.bridou_n.crossfitsolid.utils.PreferencesManager
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.gson.Gson
import io.reactivex.Completable
import io.reactivex.SingleSource
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.realm.Realm
import java.util.*
import java.util.concurrent.Callable

/**
 * Created by bridou_n on 01/08/2017.
 */

class WodsPresenter(val view: WodsContract.View,
                    val api: WodsService,
                    val passwordApi: WodsService,
                    val realm: Realm,
                    val prefs: PreferencesManager,
                    val gson: Gson) : WodsContract.Presenter, BasePresenter() {
    private val TAG = this.javaClass.simpleName

    companion object {
        const val ITEMS_PER_PAGE = 10
    }

    var disposables: CompositeDisposable = CompositeDisposable()

    override fun start() {
        val lastUpdate = prefs.getLastUpdateTime()

        view.showLastUpdate(lastUpdate)
        // If the last update is more than a day ago, automatically refresh
        if (Date().time - lastUpdate > DateUtils.DAY_IN_MILLIS) {
            Log.d(TAG, "refreshing")
            refresh()
        }
    }

    override fun refresh(paged: Int) {

        val getPassCompletable = if (prefs.hasExpiredCookies()) {
            Log.d(TAG, "Need to get cookie from password.")
            prefs.clearCookies()

            val map = mutableMapOf<String, String>()

            map["post_password"] = FirebaseRemoteConfig.getInstance().getString("wod_pass")
            passwordApi.postWodsPassword(map)
                    .doOnComplete {
                        prefs.setCookiesExpiryNow() // Update the cookies expiry
                    }
        } else {
            Log.d(TAG, "Alright, I have stored cookies! -> refreshing $paged")
            Completable.complete()
        }

        view.showLoading(true)
        disposables.add(
                getPassCompletable.toSingle({
                    api.getWods(paged).blockingGet()
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ resp ->
                    prefs.setLastUpdateTime(Date().time)
                    view.showLoading(false)
                    view.showLastUpdate(prefs.getLastUpdateTime())

                    val items = resp?.channel?.items

                    if (items != null && items.isNotEmpty()) { // Save all the items to realm
                        realm.executeTransactionAsync { tRealm ->
                            for (i in 0 until items.size) {
                                tRealm.copyToRealmOrUpdate(items[i])
                            }
                        }

                        if (items.size < ITEMS_PER_PAGE) { // If we get 6,7 items for example, means we're at the end
                            view.setNoMoreItems(true)
                        }
                    }

                    if (paged == 0) { // If we are refreshing the first page, scroll to top
                        view.scrollToTop()
                    }

                }, { err ->
                    err.printStackTrace()
                    view.showLoading(false)
                    view.showLastUpdate(prefs.getLastUpdateTime())
                    view.showError(getErrorMessage(err, gson), paged)
                }))
    }

    override fun stop() {
        view.showLoading(false)
        disposables.clear()
    }
}