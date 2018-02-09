package com.bridou_n.crossfitsolid.utils.extensionFunctions

import android.os.Build
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.view.View
import android.view.WindowManager
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

fun Fragment.setStatusBarColor(color: Int) {

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && activity != null) {
        // clear FLAG_TRANSLUCENT_STATUS flag:
        activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)

        // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        activity?.window?.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)

        // finally change the color
        activity?.window?.statusBarColor = ContextCompat.getColor(activity!!, color)
    }
}