package com.bridou_n.crossfitsolid.utils.extensionFunctions

import android.view.View
import android.view.View.*

/**
 * Created by bridou_n on 26/07/2017.
 */

fun View.hideView(keepSpace: Boolean = false) {
    visibility = if (keepSpace) INVISIBLE else GONE
}

fun View.show() {
    visibility = VISIBLE
}