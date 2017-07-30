package com.bridou_n.crossfitsolid.features

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import butterknife.BindView
import butterknife.ButterKnife
import com.bridou_n.crossfitsolid.API.BookingService
import com.bridou_n.crossfitsolid.R
import com.bridou_n.crossfitsolid.features.account.AccountFragment
import com.bridou_n.crossfitsolid.features.classes.ClassesFragment
import com.bridou_n.crossfitsolid.features.login.LoginActivity
import com.bridou_n.crossfitsolid.features.wods.WodsFragment
import com.bridou_n.crossfitsolid.utils.PreferencesManager
import com.bridou_n.crossfitsolid.utils.extensionFunctions.component
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    @BindView(R.id.bottom_navigation) lateinit var bottomNav: BottomNavigationView

    @Inject lateinit var bookingService: BookingService
    @Inject lateinit var prefs: PreferencesManager

    private val POSITION_KEY = "position"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.AppThemeNoActionBar)
        setContentView(R.layout.activity_main)
        ButterKnife.bind(this)
        component().inject(this)
        redirectToLoginIfNotLogged()

        bottomNav.setOnNavigationItemSelectedListener { item ->
            var frag: Fragment? = null

            when (item.itemId) {
                R.id.action_classes -> {
                    frag = ClassesFragment()
                }
                R.id.action_wod -> {
                    frag = WodsFragment()
                }
                R.id.action_account -> {
                    frag = AccountFragment()
                }
            }

            if (frag != null) {
                supportFragmentManager.beginTransaction().replace(R.id.fragment_container, frag).commit()
            }
            true
        }
        bottomNav.selectedItemId = savedInstanceState?.getInt(POSITION_KEY) ?: R.id.action_classes
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        outState?.putInt(POSITION_KEY, bottomNav.selectedItemId)
        super.onSaveInstanceState(outState)
    }

    fun redirectToLoginIfNotLogged() {
        if (!prefs.isLogged()) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }
}
