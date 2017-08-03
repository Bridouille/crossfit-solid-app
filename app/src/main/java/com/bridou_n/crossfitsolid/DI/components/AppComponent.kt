package com.bridou_n.crossfitsolid.DI.components

import android.content.Context
import com.bridou_n.crossfitsolid.DI.modules.AppModule
import com.bridou_n.crossfitsolid.DI.modules.NetworkModule
import com.bridou_n.crossfitsolid.DI.modules.PreferencesModule
import com.bridou_n.crossfitsolid.features.MainActivity
import com.bridou_n.crossfitsolid.features.account.AccountFragment
import com.bridou_n.crossfitsolid.features.classes.ClassesFragment
import com.bridou_n.crossfitsolid.features.classes.DayViewFragment
import com.bridou_n.crossfitsolid.features.classes.DayViewPresenter
import com.bridou_n.crossfitsolid.features.login.LoginActivity
import com.bridou_n.crossfitsolid.features.wods.WodsFragment
import com.google.gson.Gson
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
    fun providesGson() : Gson

    fun inject(act: LoginActivity)
    fun inject(act: MainActivity)

    fun inject(frag: DayViewFragment)
    fun inject(frag: WodsFragment)
    fun inject(frag: AccountFragment)
}