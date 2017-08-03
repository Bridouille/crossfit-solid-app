package com.bridou_n.crossfitsolid.features.wods

import android.support.v7.widget.RecyclerView
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import com.bridou_n.crossfitsolid.R
import com.bridou_n.crossfitsolid.models.wods.Item
import com.bridou_n.crossfitsolid.utils.extensionFunctions.getFullDate
import io.realm.RealmRecyclerViewAdapter
import io.realm.RealmResults
import java.util.*

/**
 * Created by bridou_n on 01/08/2017.
 */

class WodsRecyclerViewAdapter(val data: RealmResults<Item>) : RealmRecyclerViewAdapter<Item, RecyclerView.ViewHolder>(data, true) {

    class WodViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

        @BindView(R.id.title) lateinit var title: TextView
        @BindView(R.id.creator) lateinit var creator: TextView
        @BindView(R.id.pub_date) lateinit var pubDate: TextView
        @BindView(R.id.content) lateinit var content: TextView

        init {
            ButterKnife.bind(this, view)
        }

        fun bindView(item: Item) {
            title.text = item.title
            creator.text = item.creator
            pubDate.text = Date(item.pubDate ?: Date().time).getFullDate()

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                content.text = Html.fromHtml(item.content, Html.FROM_HTML_MODE_LEGACY)
            } else {
                content.text = Html.fromHtml(item.content)
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return WodViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_wod, parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)

        if (holder is WodViewHolder && item != null) {
            holder.bindView(item)
        }
    }
}