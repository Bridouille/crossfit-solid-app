package com.bridou_n.crossfitsolid.features.wods

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import butterknife.BindView
import butterknife.ButterKnife
import com.bridou_n.crossfitsolid.API.WodsService
import com.bridou_n.crossfitsolid.R
import com.bridou_n.crossfitsolid.models.wods.Item
import com.bridou_n.crossfitsolid.models.wods.Rss
import com.bridou_n.crossfitsolid.utils.extensionFunctions.component
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.realm.Realm
import javax.inject.Inject

/**
 * Created by bridou_n on 27/07/2017.
 */

class WodsFragment : Fragment(), WodsContract.View {

    @BindView(R.id.rv) lateinit var rv: RecyclerView

    @Inject lateinit var wodsService: WodsService

    val realm: Realm = Realm.getDefaultInstance()
    lateinit var presenter: WodsContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        component().inject(this)

        presenter = WodsPresenter(this, wodsService, realm)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val v = inflater.inflate(R.layout.fragment_wods, container, false)
        ButterKnife.bind(this, v)

        rv.layoutManager = LinearLayoutManager(context)
        rv.setHasFixedSize(true)
        rv.adapter = WodsRecyclerViewAdapter(realm.where(Item::class.java).findAllSorted("pubDate"))

        return v
    }

    override fun onResume() {
        super.onResume()
        presenter.start()
    }

    override fun onPause() {
        super.onPause()
        presenter.stop()
    }

    override fun onDestroy() {
        realm.close()
        rv.adapter = null
        super.onDestroy()
    }
}