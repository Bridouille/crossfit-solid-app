package com.bridou_n.crossfitsolid.features.wods

import android.util.Log
import com.bridou_n.crossfitsolid.API.WodsService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import io.realm.Realm

/**
 * Created by bridou_n on 01/08/2017.
 */

class WodsPresenter(val view: WodsContract.View,
                    val api: WodsService,
                    val realm: Realm) : WodsContract.Presenter {
    var disposable: Disposable? = null

    override fun start() {
        disposable = api.getWods()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ resp ->
                    val items = resp?.channel?.items

                    if (items != null && items.isNotEmpty()) {
                        realm.executeTransaction { tRealm ->
                            for (i in 0..items.size - 1) {
                                val item = items.get(i)

                                Log.d("WodsFragment", "${item.title} - ${item.creator} - ${item.pubDate} - ${item.category} - ${item.description}")

                                tRealm.copyToRealmOrUpdate(items[i])
                            }
                        }
                    }
                }, { err ->
                    // TODO handle errors
                    err.printStackTrace()
                })
    }

    override fun stop() {
        disposable?.dispose()
    }
}