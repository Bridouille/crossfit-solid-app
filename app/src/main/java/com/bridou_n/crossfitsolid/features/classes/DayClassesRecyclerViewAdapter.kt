package com.bridou_n.crossfitsolid.features.classes

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

/**
 * Created by bridou_n on 27/07/2017.
 */

class DayClassesRecyclerViewAdapter(val items: ArrayList<GroupActivity>) : RecyclerView.Adapter<DayClassesRecyclerViewAdapter.ActivitiesHolder>() {


    class ActivitiesHolder(val view: View) : RecyclerView.ViewHolder(view) {
        @BindView(R.id.title) lateinit var title: TextView
        /*@BindView(R.id.start) lateinit var start: TextView
        @BindView(R.id.duration) lateinit var duration: TextView
        @BindView(R.id.instructor) lateinit var instructor: TextView*/

        init {
            ButterKnife.bind(this, view)
        }

        fun bindView(activity: GroupActivity) {
            // TODO: adjust layout here

            title.text = activity.name
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActivitiesHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_activity, parent, false)

        return ActivitiesHolder(view)
    }

    override fun onBindViewHolder(holder: ActivitiesHolder, position: Int) {
        holder.bindView(items[position])
    }

    override fun getItemCount(): Int = items.size
}