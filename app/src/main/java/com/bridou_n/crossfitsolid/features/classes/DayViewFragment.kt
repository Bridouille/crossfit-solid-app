package com.bridou_n.crossfitsolid.features.classes

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import butterknife.BindView
import butterknife.ButterKnife
import com.bridou_n.crossfitsolid.R
import com.bridou_n.crossfitsolid.models.GroupActivity

/**
 * Created by bridou_n on 27/07/2017.
 */

class DayViewFragment : Fragment() {

    companion object {
        private val CLASSES_KEY = "classes_key"

        fun newInstance(items: ArrayList<GroupActivity>) : DayViewFragment {
            val frag = DayViewFragment()

            val args = Bundle()
            args.putParcelableArrayList(CLASSES_KEY, items)

            frag.arguments = args

            return frag
        }
    }

    @BindView(R.id.rv) lateinit var rv: RecyclerView

    lateinit var items: ArrayList<GroupActivity>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        items = arguments.getParcelableArrayList<GroupActivity>(CLASSES_KEY)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val v = inflater.inflate(R.layout.fragment_day_view, container, false)
        ButterKnife.bind(this, v)

        rv.setHasFixedSize(true)
        rv.layoutManager = LinearLayoutManager(context)
        rv.adapter = DayClassesRecyclerViewAdapter(items)

        return v
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
    }
}