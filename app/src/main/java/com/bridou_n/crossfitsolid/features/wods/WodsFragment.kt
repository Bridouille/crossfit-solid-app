package com.bridou_n.crossfitsolid.features.wods

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bridou_n.crossfitsolid.R
import com.bridou_n.crossfitsolid.features.classes.ClassesFragment

/**
 * Created by bridou_n on 27/07/2017.
 */

class WodsFragment : Fragment() {
    companion object {
        val PARAM_USER_ID = "userId"

        fun newInstance(userId: String): WodsFragment {
            val fragment = WodsFragment()
            val args = Bundle()
            args.putString(PARAM_USER_ID, userId)
            fragment.arguments = args
            return fragment
        }
    }

    lateinit var userId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            userId = arguments.getString(PARAM_USER_ID)
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater!!.inflate(R.layout.fragment_wods, container, false)
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
    }
}