package com.bridou_n.crossfitsolid.features.wods

import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.design.widget.CoordinatorLayout
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import butterknife.BindView
import butterknife.ButterKnife
import com.bridou_n.crossfitsolid.API.WodsService
import com.bridou_n.crossfitsolid.R
import com.bridou_n.crossfitsolid.models.wods.Item
import com.bridou_n.crossfitsolid.utils.PreferencesManager
import com.bridou_n.crossfitsolid.utils.copyPaste.EndlessRecyclerOnScrollListener
import com.bridou_n.crossfitsolid.utils.extensionFunctions.*
import com.google.gson.Gson
import io.realm.Realm
import io.realm.RealmResults
import io.realm.Sort
import java.util.*
import javax.inject.Inject

/**
 * Created by bridou_n on 27/07/2017.
 */

class WodsFragment : Fragment(), WodsContract.View {

    @BindView(R.id.refresh) lateinit var refresh: SwipeRefreshLayout
    @BindView(R.id.container) lateinit var container: CoordinatorLayout
    @BindView(R.id.toolbar) lateinit var toolbar: Toolbar
    @BindView(R.id.rv) lateinit var rv: RecyclerView

    @BindView(R.id.empty_container) lateinit var emptyContainer: ConstraintLayout

    @Inject lateinit var wodsService: WodsService
    @Inject lateinit var prefs: PreferencesManager
    @Inject lateinit var gson: Gson

    lateinit var presenter: WodsContract.Presenter
    val realm: Realm = Realm.getDefaultInstance()
    val rvLayoutManager: LinearLayoutManager by lazy { LinearLayoutManager(context) }
    val rvScrollListener: EndlessRecyclerOnScrollListener by lazy {
        object: EndlessRecyclerOnScrollListener(rvLayoutManager) {
            override fun onLoadMore(currentPage: Int) {
                presenter.refresh(currentPage)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        component().inject(this)

        setStatusBarColor(R.color.colorPrimaryDark)

        presenter = WodsPresenter(this, wodsService, realm, prefs, gson)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val v = inflater.inflate(R.layout.fragment_wods, container, false)
        ButterKnife.bind(this, v)

        refresh.setOnRefreshListener { presenter.refresh() }
        rv.layoutManager = rvLayoutManager
        rv.setHasFixedSize(true)
        rv.addItemDecoration(DividerItemDecoration(context, LinearLayout.VERTICAL))
        rv.addOnScrollListener(rvScrollListener)

        // Get the RealmResults to display in the adapter
        val items = realm.where(Item::class.java).findAllSortedAsync(arrayOf("pubDate"), arrayOf(Sort.DESCENDING))

        // Add a change listener to handle the "empty state"
        items.addChangeListener { newItems: RealmResults<Item> ->
            if (newItems.isLoaded) {
                showEmptyState(newItems.size == 0)
            }
        }

        rv.adapter = WodsRecyclerViewAdapter(items)

        return v
    }

    override fun onResume() {
        super.onResume()
        presenter.start()
    }

    override fun showLastUpdate(lastUpdateTime: Long) {
        var lastUpdate = getString(R.string.never)

        if (lastUpdateTime != -1.toLong()) {
            lastUpdate = Date(lastUpdateTime).getFullDateAndTime()
        }
        toolbar.subtitle = String.format(getString(R.string.last_update_x), lastUpdate)
    }

    override fun showLoading(show: Boolean) {
        if (show) {
            toolbar.setTitle(R.string.updating)
            refresh.isRefreshing = true
        } else {
            toolbar.setTitle(R.string.workout_of_the_day)
            refresh.isRefreshing = false
            rvScrollListener.setLoading(false)
        }
    }

    override fun showError(err: String?, paged: Int) {
        val snackBar = snackBar(container, err ?: getString(R.string.an_error_occurred), Snackbar.LENGTH_INDEFINITE, {
            presenter.refresh(paged)
        })

        snackBar.show()
    }

    override fun showEmptyState(show: Boolean) {
        if (show) {
            rv.hideView()
            emptyContainer.show()
        } else {
            rv.show()
            emptyContainer.hideView()
        }
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