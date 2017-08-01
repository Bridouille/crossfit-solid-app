package com.bridou_n.crossfitsolid.features.wods

import com.bridou_n.crossfitsolid.models.wods.Item

/**
 * Created by bridou_n on 01/08/2017.
 */

interface WodsContract {
    interface View {

    }

    interface Presenter {
        fun start()
        fun stop()


    }
}