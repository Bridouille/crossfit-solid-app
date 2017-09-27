package com.bridou_n.crossfitsolid

import android.app.Application
import com.bridou_n.crossfitsolid.DI.components.AppComponent
import com.bridou_n.crossfitsolid.DI.components.DaggerAppComponent
import com.bridou_n.crossfitsolid.DI.modules.ContextModule
import com.bridou_n.crossfitsolid.DI.modules.NetworkModule
import com.bridou_n.crossfitsolid.DI.modules.PreferencesModule
import com.bridou_n.crossfitsolid.utils.jobs.FetchJobCreator
import com.bridou_n.crossfitsolid.utils.jobs.FetchWodsJob
import com.evernote.android.job.JobManager
import io.realm.Realm
import io.realm.RealmConfiguration
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
                .contextModule(ContextModule(this))
                .preferencesModule(PreferencesModule())
                .networkModule(NetworkModule())
                .build()

        Realm.init(this)

        val realmConfig = RealmConfiguration.Builder()
                .deleteRealmIfMigrationNeeded()
                .build()

        Realm.setDefaultConfiguration(realmConfig)
        JodaTimeAndroid.init(this)

        JobManager.create(this).addJobCreator(FetchJobCreator())

        FetchWodsJob.scheduleJob()
    }
}