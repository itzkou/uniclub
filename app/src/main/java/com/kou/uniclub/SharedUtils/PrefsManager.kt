package com.kou.uniclub.SharedUtils

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.kou.uniclub.SharedUtils.Preferences.Companion.IS_FIRSTIME
import com.kou.uniclub.SharedUtils.Preferences.Companion.TOKEN

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

        fun seToken(context: Context, token: String?) {
            val editor = getPreferences(context).edit()
            editor.putString(TOKEN, token)
            editor.apply()
        }

        fun geToken(context: Context): String? {
            return getPreferences(context).getString(TOKEN,null)


        }



    }
}