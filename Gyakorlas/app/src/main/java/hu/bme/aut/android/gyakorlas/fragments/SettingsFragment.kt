package hu.bme.aut.android.gyakorlas.fragments

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import hu.bme.aut.android.gyakorlas.R

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
    }
}