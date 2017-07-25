package com.bridou_n.crossfitsolid.DI.components

import android.content.Context
import com.bridou_n.crossfitsolid.DI.modules.AppModule
import com.bridou_n.crossfitsolid.DI.modules.NetworkModule
import com.bridou_n.crossfitsolid.DI.modules.PreferencesModule
import com.bridou_n.crossfitsolid.features.MainActivity
import dagger.Component
import javax.inject.Singleton

/**
 * Created by bridou_n on 25/07/2017.
 */

@Singleton
@Component(modules = arrayOf(
        AppModule::class,
        NetworkModule::class,
        PreferencesModule::class
    )
)
interface AppComponent {
    fun providesContext() : Context

    fun inject(act: MainActivity)
}