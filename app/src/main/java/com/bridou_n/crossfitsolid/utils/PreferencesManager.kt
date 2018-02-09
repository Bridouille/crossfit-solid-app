package com.bridou_n.crossfitsolid.utils

import android.content.Context
import android.content.SharedPreferences
import android.text.format.DateUtils
import com.bridou_n.crossfitsolid.models.account.Profile
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.gson.Gson
import io.reactivex.Maybe
import java.util.*

/**
 * Created by bridou_n on 25/07/2017.
 */

class PreferencesManager(ctx: Context, val gson: Gson) {

    private val PREF_NAME: String = "sharedPref"
    private val prefs: SharedPreferences = ctx.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    // Various keys
    private val ACCESS_TOKEN_KEY = "pref_access_token"
    private var USERNAME_KEY = "pref_username"
    private var PASSWORD_KEY = "pref_password"
    private val USER_ID_KEY = "pref_user_id"
    private val PROFILE_KEY = "pref_profile"
    private val LAST_UPDATE_KEY = "pref_last_update"
    private val LAST_INSERTED_WOD_KEY = "pref_last_inserted_wod"
    private val COOKIES_KEY = "cookies"
    private val COOKIE_EXPIRY_KEY = "cookies_expiry"

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

    fun setLastInsertedWod(wodId: Long?) {
        if (wodId != null) {
            prefs.edit().putLong(LAST_INSERTED_WOD_KEY, wodId).apply()
        }
    }

    fun getLastInsertedWod() = prefs.getLong(LAST_INSERTED_WOD_KEY, -1)

    fun clearLastInsertedWod() = prefs.edit().putLong(LAST_INSERTED_WOD_KEY, -1).apply()

    fun setCookiesExpiryNow() = prefs.edit().putLong(COOKIE_EXPIRY_KEY, Date().time).apply()
    fun hasExpiredCookies() : Boolean {
        val cookieExpiry = prefs.getLong(COOKIE_EXPIRY_KEY, -1)

        return Date().time - cookieExpiry > (DateUtils.DAY_IN_MILLIS * FirebaseRemoteConfig.getInstance().getLong("cache_expiration_days"))
    }

    fun getCookies() = prefs.getStringSet(COOKIES_KEY, null)
    fun setCookies(set: HashSet<String>) = prefs.edit().putStringSet(COOKIES_KEY, set).apply()
    fun clearCookies() = prefs.edit().remove(COOKIES_KEY).apply()

    fun clear() = prefs.edit().clear().apply()
}