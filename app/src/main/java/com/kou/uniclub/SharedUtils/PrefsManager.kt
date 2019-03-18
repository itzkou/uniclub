package com.kou.uniclub.SharedUtils

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.kou.uniclub.SharedUtils.Preferences.Companion.IS_FIRSTIME

class PrefsManager {

    companion object {
        fun getPreferences(context: Context): SharedPreferences {
            return PreferenceManager.getDefaultSharedPreferences(context)
        }
        //isFirstime Launched

        fun setFirstime(context :Context,isFirstTime: Boolean) {
            val editor = getPreferences(context).edit()
            editor.putBoolean(IS_FIRSTIME,isFirstTime)
            editor.apply()
        }

        fun getFirstTime(context :Context): Boolean {
            return getPreferences(context).getBoolean(IS_FIRSTIME, true)
        }



    }
}