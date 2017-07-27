package com.bridou_n.crossfitsolid.features.account

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bridou_n.crossfitsolid.R

/**
 * Created by bridou_n on 27/07/2017.
 */

class AccountFragment : Fragment() {

    // TODO: try to display what we have in cache (if nothing in cache, loading) + refresh the cache
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_account, container, false)
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
    }
}
