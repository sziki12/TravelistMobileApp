package hu.bme.aut.android.gyakorlas.requestHelp

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.DialogFragment
import androidx.preference.PreferenceManager
import hu.bme.aut.android.gyakorlas.databinding.DialogRequestHelpBinding
import hu.bme.aut.android.gyakorlas.location.LocationService
import hu.bme.aut.android.gyakorlas.mapData.UserMarker
import kotlin.concurrent.thread

class RequestHelpDialogFragment() : DialogFragment(){
    private lateinit var binding: DialogRequestHelpBinding
    lateinit var listener: RequestHelpListener

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = DialogRequestHelpBinding.inflate(LayoutInflater.from(context))

        return AlertDialog.Builder(requireContext())
            .setTitle("Request help from others")
            .setView(binding.root)
            .setPositiveButton("OK") {dialog, _ ->
                val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext())
                val emailUsernameSP = requireActivity().getSharedPreferences("user_data", Context.MODE_PRIVATE)


                    val lastSavedEmail = sharedPreferences.getString("lastSavedEmail", "")
                    var username = emailUsernameSP.getString(lastSavedEmail, "") ?: ""

                    if (username == "") {
                        username = lastSavedEmail ?: "UserName"
                    }

                    if (binding.etMessage.text.toString().isNotEmpty()){
                        //listener?.onRequestHelp(HelpMessage(username, binding.etMessage.text.toString()))
                        val lat = LocationService.currentLocation?.latitude
                        val lng = LocationService.currentLocation?.longitude
                        if (lat != null && lng != null) {
                            listener?.onRequestHelp(UserMarker(username, lat, lng, binding.etMessage.text.toString()))
                        }
                    }

                dialog.dismiss()
            }
            .setNegativeButton("Cancel") {dialog, _ ->
                dialog.cancel()
            }
            .create()
    }
    companion object {
        const val TAG = "RequestHelpDialogFragment"
    }
}