package com.bridou_n.crossfitsolid.features.classes

import android.support.v4.content.ContextCompat
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import butterknife.BindView
import butterknife.BindViews
import butterknife.ButterKnife
import butterknife.OnClick
import com.bridou_n.crossfitsolid.R
import com.bridou_n.crossfitsolid.models.GroupActivity
import com.bridou_n.crossfitsolid.utils.extensionFunctions.getHourMinute
import com.bridou_n.crossfitsolid.utils.extensionFunctions.hideView
import com.bridou_n.crossfitsolid.utils.extensionFunctions.show


/**
 * Created by bridou_n on 27/07/2017.
 */

class DayClassesRecyclerViewAdapter(val items: ArrayList<GroupActivity>,
                                    val currentDate: String,
                                    val actionCallback : (Int, Boolean, Boolean, String) -> Unit) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val VIEW_TYPE_HEADER = 0
    private val VIEW_TYPE_ACTIVITY = 1

    class HeaderHolder(val view: View) : RecyclerView.ViewHolder(view) {
        @BindView(R.id.date_header) lateinit var dateTv: TextView

        init {
            ButterKnife.bind(this, view)
        }

        fun bindView(date: String) {
            dateTv.text = date
        }
    }

    class ActivityHolder(val view: View,
                         val currentDate: String,
                         val actionCallback: (Int, Boolean, Boolean, String) -> Unit) : RecyclerView.ViewHolder(view) {
        @BindView(R.id.card_view) lateinit var cardView: CardView
        @BindView(R.id.title) lateinit var title: TextView
        @BindView(R.id.subtitle) lateinit var startEnd: TextView
        @BindView(R.id.instructor) lateinit var instructor: TextView

        @BindView(R.id.slots_container) lateinit var slotsContainer: LinearLayout
        @BindView(R.id.slot_number) lateinit var slotNumber: TextView
        @BindView(R.id.slot_label) lateinit var slotLabel: TextView

        @BindView(R.id.action_btn) lateinit var actionBtn: Button
        @BindView(R.id.canceled_label) lateinit var canceled: TextView

        @BindViews(R.id.subtitle, R.id.separator, R.id.instructor, R.id.canceled_label)
        lateinit var coloredViews: Array<TextView>

        lateinit var activity: GroupActivity

        init {
            ButterKnife.bind(this, view)
        }

        enum class State(val bgColor: Int, val subtitleColor: Int) {
            TO_BOOK(R.color.bookableClassBackground, R.color.bookableClassSubtitle),
            BOOKED(R.color.bookedClassBackground, R.color.bookedClassSubtitle),
            WAITING(R.color.waitingClassBackground, R.color.waitingClassSubtitle),
            CANCELED(R.color.canceledClassBackground, R.color.canceledClassSubtitle)
        }

        fun bindView(act: GroupActivity) {
            activity = act
            title.text = activity.name
            startEnd.text = "${activity.duration?.start?.getHourMinute()} ${view.context.getString(R.string.till)} ${activity.duration?.end?.getHourMinute()}"
            instructor.text = activity.instructors?.get(0)?.name ?: view.context.getString(R.string.open_gym)

            if (instructor.text == "Joanna") {
                instructor.text = "${instructor.text} \uD83D\uDCAA"
            }

            if (activity.cancelled ?: false) { // The class has been canceled
                updateColors(State.CANCELED)

                actionBtn.hideView()
                slotsContainer.hideView()
                canceled.show()
            } else {
                if (activity.booking != null) { // We have booked the class
                    when (activity.booking?.type) {
                        "groupActivityBooking" -> { // Regular booking
                            updateColors(State.BOOKED)
                            slotNumber.text = activity.slots?.leftToBook.toString()
                            slotLabel.text = view.context.getString(R.string.slots_left)
                        }
                        "waitingListBooking" -> { // We're in the waiting list
                            updateColors(State.WAITING)
                            slotNumber.text = activity.booking?.waitingListBooking?.waitingListPosition.toString()
                            slotLabel.text = view.context.getString(R.string.people_before_you)
                        }
                    }
                    actionBtn.text = view.context.getString(R.string.cancel_my_booking)
                } else { // We haven't booked the class

                    // If there is no more spot left
                    if (activity.slots?.leftToBook ?: -1 == 0) {
                        updateColors(State.WAITING)
                        actionBtn.text = view.context.getString(R.string.join_the_waiting_list)
                        slotNumber.text = activity.slots?.inWaitingList.toString()
                        slotLabel.text = view.context.getString(R.string.in_queue)
                    } else {
                        updateColors(State.TO_BOOK)
                        actionBtn.text = view.context.getString(R.string.book_now)
                        slotNumber.text = activity.slots?.leftToBook.toString()
                        slotLabel.text = view.context.getString(R.string.slots_left)
                    }
                }

                if (activity.slots?.leftToBook ?: -1 == 0 && !(activity.slots?.hasWaitingList ?: false)) {
                    actionBtn.hideView()
                } else {
                    actionBtn.show()
                }
                slotsContainer.show()
                canceled.hideView()
            }
        }

        @OnClick(R.id.action_btn)
        fun onActionClicked() {
            val isBooked =  activity.booking != null

            val bookingId = if (isBooked) { // Id of the groupActivityBooking or waitingListBooking
                when (activity.booking?.type) {
                    "groupActivityBooking" -> activity.booking?.groupActivityBooking?.id ?: -1
                    "waitingListBooking" -> activity.booking?.waitingListBooking?.id ?: -1
                    else -> -1
                }
            } else {
                activity.id ?: -1
            }

            val isWaitingList = activity.slots?.leftToBook ?: -1 == 0 && activity.slots?.hasWaitingList ?: false
            val message = if (!isBooked) {
                String.format(view.context.getString(R.string.booking_confirmation_text), title.text, currentDate, startEnd.text, instructor.text)
            } else {
                view.context.getString(R.string.cancel_booking_confirmation)
            }

            actionCallback(bookingId, isBooked, isWaitingList, message)
        }

        fun updateColors(colors: State) {
            cardView.setCardBackgroundColor(ContextCompat.getColor(view.context, colors.bgColor))
            ButterKnife.apply(coloredViews, ButterKnife.Action { view, _ ->
                view.setTextColor(ContextCompat.getColor(view.context, colors.subtitleColor))
            })
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) VIEW_TYPE_HEADER else VIEW_TYPE_ACTIVITY
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_HEADER -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_date_header, parent, false)

                HeaderHolder(view)
            }
            else -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_activity, parent, false)

                ActivityHolder(view, currentDate, actionCallback)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is HeaderHolder -> holder.bindView(currentDate)
            is ActivityHolder -> holder.bindView(items[position - 1])
        }
    }

    override fun getItemCount(): Int = items.size + 1

    fun refreshItems(newItems: Array<GroupActivity>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }
}