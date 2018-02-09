package com.bridou_n.crossfitsolid

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.support.annotation.RequiresApi
import android.support.multidex.MultiDexApplication
import android.support.v4.content.ContextCompat
import android.util.Log
import com.bridou_n.crossfitsolid.DI.components.AppComponent
import com.bridou_n.crossfitsolid.DI.components.DaggerAppComponent
import com.bridou_n.crossfitsolid.DI.modules.ContextModule
import com.bridou_n.crossfitsolid.DI.modules.NetworkModule
import com.bridou_n.crossfitsolid.DI.modules.PreferencesModule
import com.bridou_n.crossfitsolid.utils.Utils
import com.bridou_n.crossfitsolid.utils.jobs.FetchJobCreator
import com.bridou_n.crossfitsolid.utils.jobs.FetchWodsJob
import com.bridou_n.crossfitsolid.utils.jobs.FetchWodsJob.Companion.NOTIF_CHANNEL_ID
import com.evernote.android.job.JobManager
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import io.realm.Realm
import io.realm.RealmConfiguration
import net.danlew.android.joda.JodaTimeAndroid


/**
 * Created by bridou_n on 25/07/2017.
 */

class AppSingleton : MultiDexApplication() {

    companion object {
        lateinit var component: AppComponent
    }

    private val TAG = this.javaClass.simpleName

    override fun onCreate() {
        super.onCreate()
        component = DaggerAppComponent.builder()
                .contextModule(ContextModule(this))
                .preferencesModule(PreferencesModule())
                .networkModule(NetworkModule())
                .build()

        val remoteConf = FirebaseRemoteConfig.getInstance()

        remoteConf.setConfigSettings(FirebaseRemoteConfigSettings.Builder()
                .setDeveloperModeEnabled(BuildConfig.DEBUG)
                .build())
        remoteConf.setDefaults(R.xml.firebase_remote_config)
        remoteConf.fetch(3600L).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                remoteConf.activateFetched()
                Log.d(TAG, "cache_expiration_days: " + FirebaseRemoteConfig.getInstance().getLong("cache_expiration_days"))
                Log.d(TAG, "wod_pass: ${FirebaseRemoteConfig.getInstance().getString("wod_pass")}")
            }
        }

        Realm.init(this)

        val realmConfig = RealmConfiguration.Builder()
                .deleteRealmIfMigrationNeeded()
                .build()

        Realm.setDefaultConfiguration(realmConfig)
        JodaTimeAndroid.init(this)

        if (Utils.isOreoOrLater()) {
            createNotifChannel()
        }

        JobManager.create(this).addJobCreator(FetchJobCreator())

        FetchWodsJob.scheduleJob()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotifChannel() {
        val nMgr = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val chan = NotificationChannel(NOTIF_CHANNEL_ID, getString(R.string.daily_wod), NotificationManager.IMPORTANCE_LOW)

        chan.description = getString(R.string.channel_description)

        chan.enableLights(true)
        chan.lightColor = ContextCompat.getColor(this, R.color.colorAccent)

        chan.enableVibration(false)
        chan.setBypassDnd(true)

        nMgr.createNotificationChannel(chan)
    }
}