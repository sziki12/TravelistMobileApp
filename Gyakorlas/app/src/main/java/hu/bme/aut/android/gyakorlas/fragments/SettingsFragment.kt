package hu.bme.aut.android.gyakorlas.fragments

import android.util.Log
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import hu.bme.aut.android.gyakorlas.R
import hu.bme.aut.android.gyakorlas.location.LocationService

class SettingsFragment : PreferenceFragmentCompat(), SharedPreferences.OnSharedPreferenceChangeListener{

    private lateinit var preference:SharedPreferences
    companion object
    {
        private var listeners : ArrayList<SharedPreferences.OnSharedPreferenceChangeListener> = ArrayList()

        fun registerListener(listener:SharedPreferences.OnSharedPreferenceChangeListener)
        {
            listeners.add(listener)
        }

        fun unregisterListener(listener:SharedPreferences.OnSharedPreferenceChangeListener)
        {
            listeners.remove(listener)
        }
    }
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
        preference = PreferenceManager.getDefaultSharedPreferences(this.requireContext())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        preference.registerOnSharedPreferenceChangeListener(this)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        preference.unregisterOnSharedPreferenceChangeListener(this)
    }

    override fun onSharedPreferenceChanged(preference: SharedPreferences?, s: String?) {

        if(s==null||preference==null)
            return

        Log.i("SETTINGS","Changed")
        if(s.contains("locationUpdateInterval"))
        {
            if(!preference.getString("update_location_interval","").equals("Never")&&!LocationService.isRunning)
            {
                Log.i("LOCATION","Restarting LocationService")
                requireActivity().startService(Intent(activity, LocationService::class.java))
            }
            else
            {
                Log.i("LOCATION","Else")
            }
        }


        notifyListeners(preference,s)
    }

    private fun notifyListeners(preference: SharedPreferences?, s: String?)
    {
        for(listener in listeners)
        {
            listener.onSharedPreferenceChanged(preference,s)
        }
    }
}