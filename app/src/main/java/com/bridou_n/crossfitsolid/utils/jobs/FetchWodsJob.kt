package com.bridou_n.crossfitsolid.utils.jobs

import android.util.Log
import com.bridou_n.crossfitsolid.API.WodsService
import com.bridou_n.crossfitsolid.AppSingleton
import com.bridou_n.crossfitsolid.models.wods.Item
import com.bridou_n.crossfitsolid.utils.PreferencesManager
import com.bridou_n.crossfitsolid.utils.copyPaste.DailyExecutionWindow
import com.evernote.android.job.Job
import com.evernote.android.job.JobRequest
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

            val TARGET_HOUR = hour.toLong() // 5L
            val TARGET_MINUTE = minute.toLong() // 30L
            val WINDOW_LENGTH = 0L

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

    init {
        AppSingleton.component.inject(this)
    }

    override fun onRunJob(params: Params?): Result {
        val realm: Realm = Realm.getDefaultInstance()

        try {
            if (!prefs.isLogged()) { // If we're not logged we don't continue
                return Result.FAILURE
            }

            // TODO: removes this
            prefs.clearLastInsertedWod()

            val rssResponse = wodsService.getWods()
                    .blockingGet()

            val items = rssResponse?.channel?.items

            if (items != null && items.isNotEmpty()) {
                if (prefs.getLastInsertedWod() == items[0].pubDate) {
                    // We don't have any new wod
                    Log.d(TAG, "We don't have any new wods")
                    return Result.FAILURE
                } else {
                    Log.d(TAG, "Set last inserted wod to : ${items[0].pubDate}")
                    prefs.setLastInsertedWod(items[0].pubDate)
                    prefs.setLastUpdateTime(Date().time)
                    realm.executeTransactionAsync { tRealm ->
                        for (i in 0..items.size - 1) {
                            tRealm.copyToRealmOrUpdate(items[i])
                        }
                    }
                    triggerNewWodNotification()
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

    fun triggerNewWodNotification() {
        Log.d(TAG, "Trigerring new wod notification!")
    }
}