package com.bridou_n.crossfitsolid.features

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.bridou_n.crossfitsolid.API.BookingService
import com.bridou_n.crossfitsolid.R
import com.bridou_n.crossfitsolid.models.LoginRequest
import com.bridou_n.crossfitsolid.utils.Component
import com.bridou_n.crossfitsolid.utils.PreferencesManager
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var bookingService: BookingService

    @Inject
    lateinit var prefs: PreferencesManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Component().inject(this)

        bookingService.login(LoginRequest("nicobr65@gmail.com", "toto42h"))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ resp ->
                    Log.d("hey there", "${resp.expires_in}")
                }, { err ->
                    err.printStackTrace()
                })
    }
}
