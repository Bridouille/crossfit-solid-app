package com.bridou_n.crossfitsolid.features.classes

import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import com.bridou_n.crossfitsolid.R
import com.bridou_n.crossfitsolid.models.GroupActivity
import com.bridou_n.crossfitsolid.utils.extensionFunctions.hideView
import com.bridou_n.crossfitsolid.utils.extensionFunctions.show
import java.util.*


/**
 * Created by bridou_n on 27/07/2017.
 */

class DayViewFragment : Fragment() {

    companion object {
        private val CLASSES_KEY = "classes_key"
        private val DATE_KEY = "date_key"

        fun newInstance(items: ArrayList<GroupActivity>, date: String) : DayViewFragment {
            val frag = DayViewFragment()

            val args = Bundle()
            args.putString(DATE_KEY, date)
            args.putParcelableArrayList(CLASSES_KEY, items)

            frag.arguments = args

            return frag
        }
    }

    @BindView(R.id.date_header_container) lateinit var dateHeaderCotnainer: ConstraintLayout
    @BindView(R.id.rv) lateinit var rv: RecyclerView

    @BindView(R.id.empty_container) lateinit var emptyContainer: ConstraintLayout

    lateinit var items: ArrayList<GroupActivity>
    lateinit var date: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        items = arguments.getParcelableArrayList<GroupActivity>(CLASSES_KEY)
        date = arguments.getString(DATE_KEY)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val v = inflater.inflate(R.layout.fragment_day_view, container, false)
        ButterKnife.bind(this, v)

        val dateHeaderTv = dateHeaderCotnainer.findViewById(R.id.date_header) as TextView

        dateHeaderTv?.text = date

        Log.d("DayView", "date : $date, items.size : ${items.size}")
        if (items.isEmpty()) {
            rv.hideView()
            emptyContainer.show()
        } else {
            rv.setHasFixedSize(true)
            rv.layoutManager = LinearLayoutManager(context)
            rv.adapter = DayClassesRecyclerViewAdapter(items, date)

            emptyContainer.hideView()
            rv.show()
        }
        return v
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
    }
}