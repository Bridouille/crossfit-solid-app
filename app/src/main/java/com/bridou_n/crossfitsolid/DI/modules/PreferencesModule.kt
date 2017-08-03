package com.bridou_n.crossfitsolid.DI.modules

import android.content.Context
import com.bridou_n.crossfitsolid.utils.PreferencesManager
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Created by bridou_n on 25/07/2017.
 */

@Module
class PreferencesModule {

    @Provides @Singleton
    fun providePreferencesManager(ctx: Context, gson: Gson) = PreferencesManager(ctx, gson)
}