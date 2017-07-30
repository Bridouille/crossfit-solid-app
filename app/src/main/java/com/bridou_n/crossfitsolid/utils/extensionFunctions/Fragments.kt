package com.bridou_n.crossfitsolid.utils.extensionFunctions

import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.view.View
import com.bridou_n.crossfitsolid.AppSingleton
import com.bridou_n.crossfitsolid.R

/**
 * Created by bridou_n on 27/07/2017.
 */

fun Fragment.component() = AppSingleton.component

fun Fragment.snackBar(view: View, str: String, length: Int = Snackbar.LENGTH_LONG, actionClicked: () -> Unit = { }) : Snackbar {
    return when (length) {
        Snackbar.LENGTH_INDEFINITE -> {
            Snackbar.make(view, str, length)
                    .setAction(R.string.retry, { _: View ->
                        actionClicked()
                    })
        }
        else -> {
            Snackbar.make(view, str, length)
        }
    }
}