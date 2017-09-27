package com.bridou_n.crossfitsolid.DI.modules

import android.content.Context
import com.bridou_n.crossfitsolid.AppSingleton
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Created by bridou_n on 25/07/2017.
 */

@Module class ContextModule(val app: Context) {
    @Provides @Singleton
    fun provideApp() = app
}