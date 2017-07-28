package com.bridou_n.crossfitsolid.features.classes

import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import butterknife.BindView
import butterknife.ButterKnife
import com.bridou_n.crossfitsolid.API.BookingService
import com.bridou_n.crossfitsolid.R
import com.bridou_n.crossfitsolid.models.GroupActivity
import com.bridou_n.crossfitsolid.utils.PreferencesManager
import com.bridou_n.crossfitsolid.utils.extensionFunctions.component
import javax.inject.Inject

/**
 * Created by bridou_n on 27/07/2017.
 */

class ClassesFragment : Fragment(), ClassesContract.View {

    @BindView(R.id.container) lateinit var container: ConstraintLayout
    @BindView(R.id.sliding_tabs) lateinit var tabs: TabLayout
    @BindView(R.id.viewpager) lateinit var viewPager: ViewPager

    @Inject lateinit var bookingService: BookingService
    @Inject lateinit var prefs: PreferencesManager

    lateinit var presenter: ClassesContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        component().inject(this)

        presenter = ClassesPresenter(this, bookingService, prefs)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val v = inflater.inflate(R.layout.fragment_classes, container, false)
        ButterKnife.bind(this, v)

        return v
    }

    override fun onResume() {
        super.onResume()
        presenter.start()
    }

    override fun displayClasses(bookings: Array<GroupActivity>) {
        val fragmentPagerAdpter = DaysFragmentPagerAdapter(childFragmentManager, context, bookings)
        viewPager.adapter = fragmentPagerAdpter
        tabs.setupWithViewPager(viewPager)
    }

    override fun showLoading(show: Boolean) {

    }

    override fun showError(err: String?) {
        // showSnackbar(view, err)
    }

    override fun onPause() {
        super.onPause()
        presenter.stop()
    }
}