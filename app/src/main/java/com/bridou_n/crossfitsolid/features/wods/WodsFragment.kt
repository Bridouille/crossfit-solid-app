package com.bridou_n.crossfitsolid.features.wods

import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bridou_n.crossfitsolid.API.WodsService
import com.bridou_n.crossfitsolid.R
import com.bridou_n.crossfitsolid.models.wods.Rss
import com.bridou_n.crossfitsolid.utils.extensionFunctions.component
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

/**
 * Created by bridou_n on 27/07/2017.
 */

class WodsFragment : Fragment() {

    @Inject lateinit var wodsService: WodsService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        component().inject(this)

        wodsService.getWods()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ resp ->

                    val items = resp?.channel?.items

                    if (items != null && items.isNotEmpty()) {
                        for (i in 0..items.size - 1) {
                            val item = items.get(i)

                            Log.d("WodsFragment", "${item.title} - ${item.creator} - ${item.pubDate} - ${item.category} - ${item.description}")
                        }
                    }
                }, { err ->
                    err.printStackTrace()
                })
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_wods, container, false)
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
    }
}