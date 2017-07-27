package com.bridou_n.crossfitsolid.utils.extensionFunctions

import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.view.View
import com.bridou_n.crossfitsolid.AppSingleton

/**
 * Created by bridou_n on 27/07/2017.
 */

fun Fragment.component() = AppSingleton.component

fun Fragment.showSnackbar(view: View, str: String) = Snackbar.make(view, str, Snackbar.LENGTH_LONG).show()