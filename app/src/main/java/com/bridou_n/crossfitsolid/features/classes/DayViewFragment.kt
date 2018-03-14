package com.bridou_n.crossfitsolid.features.classes

import android.content.DialogInterface
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.design.widget.CoordinatorLayout
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import com.bridou_n.crossfitsolid.API.BookingService
import com.bridou_n.crossfitsolid.R
import com.bridou_n.crossfitsolid.models.classes.GroupActivity
import com.bridou_n.crossfitsolid.utils.PreferencesManager
import com.bridou_n.crossfitsolid.utils.extensionFunctions.*
import com.google.gson.Gson
import com.jakewharton.rxbinding2.view.RxView
import io.reactivex.Observable
import org.joda.time.LocalDate
import javax.inject.Inject


/**
 * Created by bridou_n on 27/07/2017.
 */

class DayViewFragment : Fragment(), DayViewContract.View {

    companion object {
        private val POS_KEY = "pos_key"

        fun newInstance(position: Int) : DayViewFragment {
            val frag = DayViewFragment()

            val args = Bundle()
            args.putInt(POS_KEY, position)

            frag.arguments = args

            return frag
        }
    }

    @BindView(R.id.day_view_container) lateinit var dayViewContainer: CoordinatorLayout
    @BindView(R.id.refresh) lateinit var refreshLayout: SwipeRefreshLayout
    @BindView(R.id.date_header_container) lateinit var dateHeaderContainer: ConstraintLayout
    @BindView(R.id.date_header) lateinit  var dateHeaderTv: TextView
    @BindView(R.id.rv) lateinit var rv: RecyclerView

    @BindView(R.id.empty_container) lateinit var emptyContainer: ConstraintLayout

    @BindView(R.id.loading_container) lateinit var loadingContainer: ConstraintLayout

    @BindView(R.id.error_container) lateinit var errorContainer: ConstraintLayout
    @BindView(R.id.error_text) lateinit var errorText: TextView
    @BindView(R.id.retry_btn) lateinit var retryBtn: Button

    @Inject lateinit var bookingService: BookingService
    @Inject lateinit var prefs: PreferencesManager
    @Inject lateinit var gson: Gson

    var position: Int? = null
    lateinit var currentDate: LocalDate
    lateinit var adapter: DayClassesRecyclerViewAdapter
    lateinit var presenter: DayViewPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        component().inject(this)

        position = arguments?.getInt(POS_KEY)
        currentDate = LocalDate.now().plusDays(position ?: 0)
        presenter = DayViewPresenter(this, bookingService, gson, prefs, currentDate)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val v = inflater.inflate(R.layout.fragment_day_view, container, false)
        ButterKnife.bind(this, v)

        refreshLayout.setOnRefreshListener { presenter.refresh() }
        dateHeaderTv.text = currentDate.toDate().getFullDate()

        rv.setHasFixedSize(true)
        rv.layoutManager = LinearLayoutManager(context)
        adapter = DayClassesRecyclerViewAdapter(ArrayList(), currentDate.toDate().getFullDate(), {
            bookingId, isBooked, isWaitingList, message ->

            Log.d("DayViewFragment", "bookingId: $bookingId -isBooked: $isBooked - isWaitingList: $isWaitingList")

            val builder = AlertDialog.Builder(activity!!, R.style.AppCompatAlertDialogStyle)
                    .setTitle(getString(R.string.confirm))
                    .setMessage(message)
                    .setPositiveButton(getString(android.R.string.ok), DialogInterface.OnClickListener {
                        _, _ -> presenter.bookClass(bookingId, isBooked, isWaitingList)
                    })
                    .setNegativeButton(getString(android.R.string.cancel), null)

            builder.show()
        })
        rv.adapter = adapter

        return v
    }

    override fun onResume() {
        super.onResume()
        presenter.start()
    }

    fun hideAll() {
        refreshLayout.isRefreshing = false
        loadingContainer.hideView()
        emptyContainer.hideView()
        errorContainer.hideView()
        rv.hideView()
    }

    override fun showLoading(show: Boolean) {
        hideAll()
        if (show) {
            loadingContainer.show()
        }
    }

    override fun showRefresh(show: Boolean) {
        refreshLayout.isRefreshing = show
    }

    override fun showEmptyView() {
        hideAll()
        emptyContainer.show()
    }

    override fun displayClasses(bookings: Array<GroupActivity>) {
        hideAll()
        adapter.refreshItems(bookings)
        rv.show()
    }

    override fun showError(err: String?) : Observable<Int> {
        hideAll()
        errorContainer.show()
        errorText.text = err ?: context!!.getString(R.string.an_error_occurred)

        return RxView.clicks(retryBtn).map { _ -> 1 }
    }

    override fun showSmallError(err: String?) {
        snackBar(dayViewContainer, err ?: getString(R.string.an_error_occurred)).show()
    }

    override fun onPause() {
        presenter.stop()
        super.onPause()
    }
}