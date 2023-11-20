package hu.bme.aut.android.gyakorlas.fragments

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import androidx.fragment.app.DialogFragment
import hu.bme.aut.android.gyakorlas.GeofenceRadiusListener
import hu.bme.aut.android.gyakorlas.databinding.DialogGeofenceRadiusBinding
import hu.bme.aut.android.gyakorlas.mapData.GeofenceHandler
import java.lang.RuntimeException

class GeofenceRadiusDialogFragment : DialogFragment(){
    private lateinit var binding: DialogGeofenceRadiusBinding
    lateinit var listener: GeofenceRadiusListener

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = DialogGeofenceRadiusBinding.inflate(LayoutInflater.from(context))

        return AlertDialog.Builder(requireContext())
            .setTitle("Set the distance for recommended places")
            .setView(binding.root)
            .setPositiveButton("OK") {dialog, _ ->
                val distanceText = binding.etDistance.text.toString()

                if (distanceText.isNotEmpty() && distanceText.toFloat() >= 0){
                    GeofenceHandler.geofenceRadius = distanceText.toFloat()
                    listener?.onGeofenceRadiusChanged()
                }
                dialog.dismiss()
            }
            .setNegativeButton("Cancel") {dialog, _ ->
                dialog.cancel()
            }
            .create()
    }
    companion object {
        const val TAG = "GeofenceRadiusDialogFragment"
    }
}