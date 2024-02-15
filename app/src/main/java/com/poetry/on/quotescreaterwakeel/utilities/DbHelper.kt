package com.poetry.on.quotescreaterwakeel.utilities

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import java.io.File

class DbHelper(val context: Context) {

    fun userExists(): Boolean {
        return File("/data/data/" + context.packageName + "/shared_prefs/" + SHARED_PREFS + ".xml").exists()
    }

    fun chkBroadCast(str: String?): Boolean {
        return context.getSharedPreferences(SHARED_PREFS, 0).getBoolean(str, false)
    }

    fun setBroadCast(str: String?, z: Boolean) {
        val edit = context.getSharedPreferences(SHARED_PREFS, 0).edit()
        edit.putBoolean(str, z)
        edit.apply()
    }


    fun setAlarmSetting(str: String?, bool: Boolean?) {
        val edit = context.getSharedPreferences(SHARED_PREFS, 0).edit()
        edit.putBoolean(str, bool!!)
        edit.apply()
    }

    fun getAlarmSetting(str: String?): Boolean {
        return context.getSharedPreferences(SHARED_PREFS, 0).getBoolean(str, false)
    }

    fun setAppPurchase(z: Boolean, appCompatActivity: AppCompatActivity) {
        val edit = appCompatActivity.getSharedPreferences(SHARED_PREFS, 0).edit()
        edit.putBoolean(Apppurchase, z)
        edit.apply()
    }

    fun getAppPurchase(context: Context): Boolean {
        return context.getSharedPreferences(SHARED_PREFS, 0)
            .getBoolean(Apppurchase, false)
    }

    fun getBooleanData(context: Context, key: String?, value: Boolean): Boolean {
        return context.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE)
            .getBoolean(key, value)
    }


    fun getIntData(context: Context, key: String?, value: Int): Int {
        return context.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE)
            .getInt(key, value)
    }


    // Get Data
    fun getStringData(context: Context, key: String?, value: String): String? {
        return context.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE)
            .getString(key, value)
    }

    // Save Data
    fun saveData(context: Context, key: String?, `val`: String?) {
        context.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE).edit()
            .putString(key, `val`).apply()
    }

    fun saveData(context: Context, key: String?, `val`: Int) {
        context.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE).edit()
            .putInt(key, `val`)
            .apply()
    }

    fun saveData(context: Context, key: String?, `val`: Boolean) {
        context.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE)
            .edit()
            .putBoolean(key, `val`)
            .apply()
    }

    fun getSharedPrefEditor(context: Context, pref: String?): SharedPreferences.Editor {
        return context.getSharedPreferences(pref, Context.MODE_PRIVATE).edit()
    }

    fun saveData(editor: SharedPreferences.Editor) {
        editor.apply()
    }

    companion object {
        const val AD_PREFS = "adspref"
        fun setRemoteValue(context: Context, str: String?, z: Boolean) {
            val edit = context.getSharedPreferences(AD_PREFS, 0).edit()
            edit.putBoolean(str, z)
            edit.apply()
        }

        fun getRemoteValue(context: Context, str: String?): Boolean {
            return context.getSharedPreferences(AD_PREFS, 0).getBoolean(str, true)
        }

        fun setRemoteCounter(context: Context, str: String?, j: Long) {
            val edit = context.getSharedPreferences(AD_PREFS, 0).edit()
            edit.putLong(str, j)
            edit.apply()
        }

        fun getRemoteCounter(context: Context, str: String?): Long {
            return context.getSharedPreferences(AD_PREFS, 0).getLong(str, 5L)
        }


    }
}