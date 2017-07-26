package com.bridou_n.crossfitsolid.utils

import android.content.Context
import android.content.SharedPreferences

/**
 * Created by bridou_n on 25/07/2017.
 */

class PreferencesManager(ctx: Context) {

    val PREF_NAME: String = "sharedPref"
    val prefs: SharedPreferences = ctx.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    // Various keys
    val ACCESS_TOKEN_KEY = "pref_access_token"
    val REFRESH_TOKEN_KEY = "pref_refresh_token"
    var USERNAME_KEY = "pref_username"
    var PASSWORD_KEY = "pref_password"

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

    fun isLogged() : Boolean = getToken() != null

    fun clear() = prefs.edit().clear().apply()
}