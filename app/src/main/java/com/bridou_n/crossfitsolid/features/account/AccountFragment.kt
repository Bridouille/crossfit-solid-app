package com.bridou_n.crossfitsolid.features.account

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import butterknife.ButterKnife
import butterknife.OnClick
import com.bridou_n.crossfitsolid.R
import com.bridou_n.crossfitsolid.features.login.LoginActivity
import com.bridou_n.crossfitsolid.utils.PreferencesManager
import com.bridou_n.crossfitsolid.utils.extensionFunctions.component
import javax.inject.Inject

/**
 * Created by bridou_n on 27/07/2017.
 */

class AccountFragment : Fragment() {

    @Inject lateinit var prefs: PreferencesManager

    // TODO: try to display what we have in cache (if nothing in cache, loading) + refresh the cache
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        component().inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val v = inflater.inflate(R.layout.fragment_account, container, false)
        ButterKnife.bind(this, v)

        return v
    }

    override fun onResume() {
        super.onResume()
    }

    @OnClick(R.id.logout_btn)
    fun onLogoutClicked() {
        prefs.clear()
        startActivity(Intent(activity, LoginActivity::class.java))
        activity.finish()
    }

    override fun onPause() {
        super.onPause()
    }
}
