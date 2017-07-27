package com.bridou_n.crossfitsolid.features

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import android.util.Log
import butterknife.BindView
import butterknife.ButterKnife
import com.bridou_n.crossfitsolid.API.BookingService
import com.bridou_n.crossfitsolid.R
import com.bridou_n.crossfitsolid.features.login.LoginActivity
import com.bridou_n.crossfitsolid.utils.PreferencesManager
import com.bridou_n.crossfitsolid.utils.extensionFunctions.component
import com.bridou_n.crossfitsolid.utils.extensionFunctions.toIso8601Format
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.*
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

        if (!prefs.isLogged()) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        /*bookingService.getProfile("1480")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ profile ->
                    Log.d("MainActivity", "$profile")
                    Log.d("MainActivity", "${profile.billingAddress}" + profile.billingAddress?.city)
                }, { err ->
                    err.printStackTrace()
                })*/

        val start = Date()
        val end = Date(start.time + 84600000)


        bookingService.getGroupActivites(startDate = start.toIso8601Format(), endDate = end.toIso8601Format())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ resp ->

                    for (ga in resp) {
                        Log.d("MainAct", "${ga.id} -- ${ga.name}")
                    }

                }, { err ->
                    err.printStackTrace()
                })

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
