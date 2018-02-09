package com.bridou_n.crossfitsolid.utils

import android.os.Build

/**
 * Created by bridou_n on 18/02/2018.
 */

class Utils {
    companion object {
        fun isLOrLater() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP

        fun isMOrLater() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.M

        fun isNOrLater() =  Build.VERSION.SDK_INT >= Build.VERSION_CODES.N

        fun isOreoOrLater() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.O

    }
}