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
import com.bridou_n.crossfitsolid.R
import com.bridou_n.crossfitsolid.utils.extensionFunctions.setStatusBarColor

/**
 * Created by bridou_n on 27/07/2017.
 */

class ClassesFragment : Fragment() {

    @BindView(R.id.container) lateinit var container: ConstraintLayout
    @BindView(R.id.sliding_tabs) lateinit var tabs: TabLayout
    @BindView(R.id.viewpager) lateinit var viewPager: ViewPager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStatusBarColor(R.color.divider)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val v = inflater.inflate(R.layout.fragment_classes, container, false)
        ButterKnife.bind(this, v)

        viewPager.adapter = DaysFragmentPagerAdapter(childFragmentManager, context)
        viewPager.offscreenPageLimit = viewPager.adapter.count
        tabs.setupWithViewPager(viewPager)

        return v
    }
}