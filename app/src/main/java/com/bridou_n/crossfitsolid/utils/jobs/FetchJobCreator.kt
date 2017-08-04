package com.bridou_n.crossfitsolid.utils.jobs

import com.evernote.android.job.Job
import com.evernote.android.job.JobCreator

/**
 * Created by bridou_n on 04/08/2017.
 */

class FetchJobCreator : JobCreator {
    override fun create(tag: String?): Job? {
        return when (tag) {
            FetchWodsJob.TAG -> FetchWodsJob()
            else -> null
        }
    }
}