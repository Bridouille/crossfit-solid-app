package com.bridou_n.crossfitsolid.features.classes

import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bridou_n.crossfitsolid.API.BookingService
import com.bridou_n.crossfitsolid.R
import com.bridou_n.crossfitsolid.utils.extensionFunctions.component
import com.bridou_n.crossfitsolid.utils.extensionFunctions.toIso8601Format
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.*
import javax.inject.Inject

/**
 * Created by bridou_n on 27/07/2017.
 */

class ClassesFragment : Fragment() {
    companion object {
        val PARAM_USER_ID = "userId"

        fun newInstance(userId: String): ClassesFragment {
            val fragment = ClassesFragment()
            val args = Bundle()
            args.putString(PARAM_USER_ID, userId)
            fragment.arguments = args
            return fragment
        }
    }

    @Inject lateinit var bookingService: BookingService

    lateinit var userId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        component().inject(this)

        if (arguments != null) {
            userId = arguments.getString(PARAM_USER_ID)
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater!!.inflate(R.layout.fragment_classes, container, false)
    }

    override fun onResume() {
        super.onResume()

        val start = Date()
        val end = Date(start.time + 84600000)

        bookingService.getGroupActivites(startDate = start.toIso8601Format(), endDate = end.toIso8601Format())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ resp ->

                    for (ga in resp) {
                        Log.d("ClassesFragment", "${ga.id} -- ${ga.name}")
                    }

                }, { err ->
                    err.printStackTrace()
                })

    }

    override fun onPause() {
        super.onPause()
    }
}