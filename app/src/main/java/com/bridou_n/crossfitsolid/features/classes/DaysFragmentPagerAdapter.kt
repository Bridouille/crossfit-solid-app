package com.bridou_n.crossfitsolid.features.classes

import android.content.Context
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.bridou_n.crossfitsolid.R
import com.bridou_n.crossfitsolid.utils.extensionFunctions.getDayString
import org.joda.time.LocalDate

/**
 * Created by bridou_n on 27/07/2017.
 */

class DaysFragmentPagerAdapter(fm: FragmentManager,
                               val ctx: Context) : FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        return DayViewFragment.newInstance(position)
    }

    override fun getCount(): Int {
        return 7 // We only display data for one week
    }

    override fun getPageTitle(position: Int): CharSequence {
        return when (position) {
            0 -> ctx.getString(R.string.today)
            1 -> ctx.getString(R.string.tomorrow)
            else -> {
                LocalDate.now().plusDays(position).toDate().getDayString()
            }
        }
    }
}