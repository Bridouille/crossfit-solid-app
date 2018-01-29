package com.bridou_n.crossfitsolid.features.wods

import android.text.format.DateUtils
import com.bridou_n.crossfitsolid.API.WodsService
import com.bridou_n.crossfitsolid.utils.BasePresenter
import com.bridou_n.crossfitsolid.utils.PreferencesManager
import com.google.gson.Gson
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.realm.Realm
import java.util.*

/**
 * Created by bridou_n on 01/08/2017.
 */

class WodsPresenter(val view: WodsContract.View,
                    val api: WodsService,
                    val realm: Realm,
                    val prefs: PreferencesManager,
                    val gson: Gson) : WodsContract.Presenter, BasePresenter() {
    var disposables: CompositeDisposable = CompositeDisposable()

    override fun start() {
        val lastUpdate = prefs.getLastUpdateTime()

        view.showLastUpdate(lastUpdate)
        // If the last update is more than a day ago, automatically refresh
        if (Date().time - lastUpdate > DateUtils.DAY_IN_MILLIS) {
            refresh()
        }
    }

    override fun refresh(paged: Int) {

        view.showLoading(true)
        disposables.add(api.getWods(paged)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ resp ->
                    prefs.setLastUpdateTime(Date().time)
                    view.showLoading(false)
                    view.showLastUpdate(prefs.getLastUpdateTime())
                    if (paged == 0) { // If we are refreshing the first page, scroll to top
                        view.scrollToTop()
                    }

                    val items = resp?.channel?.items

                    if (items != null && items.isNotEmpty()) {
                        realm.executeTransaction { tRealm ->
                            for (i in 0..items.size - 1) {
                                tRealm.copyToRealmOrUpdate(items[i])
                            }
                        }
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