package com.bridou_n.crossfitsolid.utils.jobs

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import android.support.v4.content.ContextCompat
import android.support.v7.app.NotificationCompat
import android.util.Log
import com.bridou_n.crossfitsolid.API.WodsService
import com.bridou_n.crossfitsolid.BuildConfig
import com.bridou_n.crossfitsolid.DI.components.DaggerAppComponent
import com.bridou_n.crossfitsolid.DI.modules.ContextModule
import com.bridou_n.crossfitsolid.DI.modules.NetworkModule
import com.bridou_n.crossfitsolid.DI.modules.PreferencesModule
import com.bridou_n.crossfitsolid.R
import com.bridou_n.crossfitsolid.features.MainActivity
import com.bridou_n.crossfitsolid.models.wods.Item
import com.bridou_n.crossfitsolid.utils.PreferencesManager
import com.bridou_n.crossfitsolid.utils.copyPaste.DailyExecutionWindow
import com.bridou_n.crossfitsolid.utils.extensionFunctions.getIso8601Format
import com.evernote.android.job.Job
import com.evernote.android.job.JobRequest
import com.google.gson.GsonBuilder
import io.realm.Realm
import retrofit2.HttpException
import java.io.IOException
import java.util.*
import javax.inject.Inject


/**
 * Created by bridou_n on 04/08/2017.
 */

class FetchWodsJob : Job() {

    companion object {
        const val TAG = "fetch_wods_job_tag"

        fun scheduleJob(updateCurrent: Boolean = true) : Int {
            val calendar = Calendar.getInstance()
            val hour = calendar.get(Calendar.HOUR_OF_DAY)
            val minute = calendar.get(Calendar.MINUTE)

            // Between 4:15am and 5:15am
            val TARGET_HOUR = if (BuildConfig.DEBUG) hour.toLong() else 4L
            val TARGET_MINUTE = if (BuildConfig.DEBUG) minute.toLong() + 1 else 15L
            val WINDOW_LENGTH = if (BuildConfig.DEBUG) 0 else 60L

            val executionWindow = DailyExecutionWindow(hour, minute, TARGET_HOUR, TARGET_MINUTE, WINDOW_LENGTH)

            return JobRequest.Builder(TAG)
                    .setExecutionWindow(executionWindow.startMs, executionWindow.endMs)
                    .setPersisted(true)
                    .setUpdateCurrent(updateCurrent)
                    .setRequiredNetworkType(JobRequest.NetworkType.CONNECTED)
                    .setRequiresCharging(true)
                    .setRequiresDeviceIdle(true)
                    .build()
                    .schedule()
        }
    }

    @Inject lateinit var wodsService: WodsService
    @Inject lateinit var prefs: PreferencesManager

    override fun onRunJob(params: Params?): Result {
        Log.d(TAG, "onRunJob()")
        DaggerAppComponent.builder()
                .contextModule(ContextModule(context)) // Getting the context from the job and not injecting with the AppSingleton component
                .preferencesModule(PreferencesModule())
                .networkModule(NetworkModule())
                .build()
                .inject(this)

        val realm: Realm = Realm.getDefaultInstance()

        try {
            if (!prefs.isLogged()) { // If we're not logged we don't continue
                return Result.FAILURE
            }

            Log.d(TAG, "We're logged!")
            val rssResponse = wodsService.getWods()
                    .blockingGet()

            val items = rssResponse?.channel?.items

            if (items != null && items.isNotEmpty()) {
                prefs.setLastUpdateTime(Date().time)

                if (BuildConfig.DEBUG) { // To fire the notification at all time
                    prefs.clearLastInsertedWod()
                }

                if (prefs.getLastInsertedWod() == items[0].pubDate) {
                    // We don't have any new wod
                    Log.d(TAG, "We don't have any new wods")
                    return Result.FAILURE
                } else {
                    Log.d(TAG, "Set last inserted wod to : ${items[0].pubDate}")
                    prefs.setLastInsertedWod(items[0].pubDate)
                    realm.executeTransaction { tRealm ->
                        for (i in 0..items.size - 1) {
                            tRealm.copyToRealmOrUpdate(items[i])
                        }
                    }
                    triggerNewWodNotification(items[0])
                    return Result.SUCCESS
                }
            }
            return Result.RESCHEDULE // Error in the api response ?
        } catch (e: Exception) {
            return when (e) {
                is HttpException, is IOException -> Result.RESCHEDULE // Probably a network error or something, retry soon
                else -> Result.FAILURE // If any other unknown response occur, don't reschedule
            }
        } finally {
            scheduleJob(false) // Don't update current, it would cancel this currently running job
        }
    }

    fun triggerNewWodNotification(item: Item) {
        Log.d(TAG, "Triggering new wod notification!")

        val resultIntent = Intent(context, MainActivity::class.java)
        resultIntent.putExtra(MainActivity.ACTIVE_TAB, R.id.action_wod)

        val contentIntent = PendingIntent.getActivity(context, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        val mBuilder = NotificationCompat.Builder(context)
                .setSmallIcon(R.mipmap.ic_notification_logo)
                .setContentTitle(context.getString(R.string.todays_wod))
                .setContentText(String.format(context.getString(R.string.checkout_the_wod_for_x), item.title))
                .setContentIntent(contentIntent)
                .setAutoCancel(true)
                .setColor(ContextCompat.getColor(context, R.color.colorPrimary))

        // Sets an ID for the notification
        val mNotificationId = 1
        // Gets an instance of the NotificationManager service
        val mNotifyMgr = context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        // Builds the notification and issues it.
        mNotifyMgr.notify(mNotificationId, mBuilder.build())
    }
}