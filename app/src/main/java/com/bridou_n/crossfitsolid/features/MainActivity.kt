package com.bridou_n.crossfitsolid.features

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import butterknife.BindView
import butterknife.ButterKnife
import com.bridou_n.crossfitsolid.API.BookingService
import com.bridou_n.crossfitsolid.R
import com.bridou_n.crossfitsolid.features.login.LoginActivity
import com.bridou_n.crossfitsolid.utils.PreferencesManager
import com.bridou_n.crossfitsolid.utils.extensionFunctions.component
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    @BindView(R.id.bottom_navigation) lateinit var bottomNav: BottomNavigationView

    @Inject lateinit var bookingService: BookingService
    @Inject lateinit var prefs: PreferencesManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.AppThemeNoActionBar)
        setContentView(R.layout.activity_main)
        ButterKnife.bind(this)
        component().inject(this)

        if (!prefs.isLogged()) { // TODO: make sure the Authenticator works on 401 after token expires..
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        bottomNav.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.action_classes -> {

                }
                R.id.action_wod -> {

                }
                R.id.action_account -> {

                }
            }
            true
        }
    }
}
