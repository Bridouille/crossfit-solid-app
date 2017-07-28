package com.bridou_n.crossfitsolid

import android.app.Application
import com.bridou_n.crossfitsolid.DI.components.AppComponent
import com.bridou_n.crossfitsolid.DI.components.DaggerAppComponent
import com.bridou_n.crossfitsolid.DI.modules.AppModule
import com.bridou_n.crossfitsolid.DI.modules.NetworkModule
import com.bridou_n.crossfitsolid.DI.modules.PreferencesModule
import net.danlew.android.joda.JodaTimeAndroid

/**
 * Created by bridou_n on 25/07/2017.
 */

class AppSingleton : Application() {

    companion object {
        lateinit var component: AppComponent
    }

    override fun onCreate() {
        super.onCreate()
        component = DaggerAppComponent.builder()
                .appModule(AppModule(this))
                .preferencesModule(PreferencesModule())
                .networkModule(NetworkModule())
                .build()

        JodaTimeAndroid.init(this)
    }
}