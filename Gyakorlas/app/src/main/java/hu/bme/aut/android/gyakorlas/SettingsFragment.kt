package hu.bme.aut.android.gyakorlas

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat

class SettingsFragment : PreferenceFragmentCompat() {

    companion object
    {
        var preferences: SharedPreferences? = null
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
        preferences = this.preferenceManager.sharedPreferences
    }
}