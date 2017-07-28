package com.bridou_n.crossfitsolid.features.classes

import android.content.Context
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.util.Log
import com.bridou_n.crossfitsolid.R
import com.bridou_n.crossfitsolid.models.GroupActivity
import com.bridou_n.crossfitsolid.utils.extensionFunctions.getDayString
import com.bridou_n.crossfitsolid.utils.extensionFunctions.getFullDate
import org.joda.time.LocalDate

/**
 * Created by bridou_n on 27/07/2017.
 */

class DaysFragmentPagerAdapter(fm: FragmentManager,
                               val ctx: Context,
                               val data: Array<GroupActivity>) : FragmentPagerAdapter(fm) {

    val days = mutableMapOf<Int, ArrayList<GroupActivity>>()

    init {
        for (ga in data) {
            val start = LocalDate.fromDateFields(ga.duration?.start)

            if (days[start.dayOfWeek] == null) {
                days[start.dayOfWeek] = ArrayList()
            }
            days[start.dayOfWeek]?.add(ga)
        }
    }

    override fun getItem(position: Int): Fragment {
        val date =  LocalDate.now().plusDays(position)

        Log.d("Adapter", "position : $position, dayOfWeek : ${date.dayOfWeek}, items: ${days[date.dayOfWeek]}")

        return DayViewFragment.newInstance(days[date.dayOfWeek] ?: arrayListOf<GroupActivity>(), date.toDate().getFullDate())
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