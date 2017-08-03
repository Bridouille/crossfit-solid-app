package com.bridou_n.crossfitsolid.utils

import android.content.Context
import android.content.SharedPreferences
import com.bridou_n.crossfitsolid.models.account.Profile
import com.google.gson.Gson
import io.reactivex.Maybe
import io.reactivex.Single

/**
 * Created by bridou_n on 25/07/2017.
 */

class PreferencesManager(ctx: Context, val gson: Gson) {

    val PREF_NAME: String = "sharedPref"
    val prefs: SharedPreferences = ctx.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    // Various keys
    val ACCESS_TOKEN_KEY = "pref_access_token"
    var USERNAME_KEY = "pref_username"
    var PASSWORD_KEY = "pref_password"
    val USER_ID_KEY = "pref_user_id"
    val PROFILE_KEY = "pref_profile"
    val LAST_UPDATE_KEY = "pref_last_update"

    fun setUsername(username: String) {
        prefs.edit().putString(USERNAME_KEY, username).apply()
    }

    fun getUsername() : String? = prefs.getString(USERNAME_KEY, null)

    fun setPassword(password: String) {
        prefs.edit().putString(PASSWORD_KEY, password).apply()
    }

    fun getPassword() : String? = prefs.getString(PASSWORD_KEY, null)

    fun setToken(token: String) {
        prefs.edit().putString(ACCESS_TOKEN_KEY, token).apply()
    }

    fun getToken() : String? = prefs.getString(ACCESS_TOKEN_KEY, null)

    fun setUserId(userId: String) {
        prefs.edit().putString(USER_ID_KEY, userId).apply()
    }

    fun getUserId() : String? = prefs.getString(USER_ID_KEY, null)

    fun isLogged() : Boolean = getToken() != null

    fun setProfile(profile: Profile) {
        val profileStr = gson.toJson(profile)

        prefs.edit().putString(PROFILE_KEY, profileStr).apply()
    }

    fun getProfile() : Maybe<Profile> {
        val profile = gson.fromJson(prefs.getString(PROFILE_KEY, null), Profile::class.java)

        return if (profile == null) Maybe.empty() else Maybe.just(profile)
    }

    fun setLastUpdateTime(lastUpdate: Long) = prefs.edit().putLong(LAST_UPDATE_KEY, lastUpdate).apply()

    fun getLastUpdateTime() = prefs.getLong(LAST_UPDATE_KEY, -1)

    fun clear() = prefs.edit().clear().apply()
}