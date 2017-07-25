package com.bridou_n.crossfitsolid.features

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.bridou_n.crossfitsolid.API.BookingService
import com.bridou_n.crossfitsolid.R
import com.bridou_n.crossfitsolid.features.login.LoginActivity
import com.bridou_n.crossfitsolid.utils.PreferencesManager
import com.bridou_n.crossfitsolid.utils.component
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var bookingService: BookingService

    @Inject
    lateinit var prefs: PreferencesManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.AppThemeNoActionBar)
        setContentView(R.layout.activity_main)
        component().inject(this)

        prefs.clear()

        if (!prefs.isLogged()) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        // TODO: implement this
    }
}
