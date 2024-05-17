package hu.bme.aut.android.gyakorlas.comment

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import android.widget.DatePicker
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.preference.PreferenceManager
import hu.bme.aut.android.gyakorlas.databinding.DialogNewCommentBinding
import hu.bme.aut.android.gyakorlas.getCurrentUser
import hu.bme.aut.android.gyakorlas.mapData.MapDataProvider
import hu.bme.aut.android.gyakorlas.mapData.PlaceData
import java.util.Calendar
import kotlin.concurrent.thread

class EditOrNewCommentDialog(val markerID :Int,val successCallback:()->Unit) : DialogFragment(), AdapterView.OnItemSelectedListener {
    private lateinit var binding:DialogNewCommentBinding
    private var selectedIndex = 0
    private val mapDataProvider = MapDataProvider.instance
    private var username = ""
    private var lastSavedEmail = ""
    private var place: PlaceData? = null
    private var editing = false

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = DialogNewCommentBinding.inflate(LayoutInflater.from(context))
        editing=false
        thread{
            place = mapDataProvider.getMarkerByID(markerID).place

            val (email, user) = requireActivity().getCurrentUser()
            lastSavedEmail = email
            username = user

            val comment = place?.getUserComment(lastSavedEmail)
            if(comment!=null)
            {
                editing=true
                requireActivity().runOnUiThread()
                {

                    binding.newCommentRating.rating=comment.rating
                    binding.editTextComment.setText(comment.description)

                }
            }
        }

    return AlertDialog.Builder(requireContext())
    .setTitle("New Comment")
    .setView(binding.root)
    .setPositiveButton("Ok") { dialogInterface, i ->
        if(place==null)
            return@setPositiveButton
        if(binding.newCommentRating.rating.isFinite()&&binding.newCommentRating.rating>0)
        {
            var comment = ""
            if(binding.editTextComment.text.isNotEmpty())
                comment = binding.editTextComment.text.toString()
            val rating = binding.newCommentRating.rating
            thread {
                if(editing)
                    place?.removeComment(lastSavedEmail)

                place?.addComment(Comment(username,comment,rating,lastSavedEmail))

                Log.i("Comments","Title $username $comment $rating")
                Log.i("Comments","Comments: ${place?.comments?.size}")
                requireActivity().runOnUiThread()
                {
                    successCallback()
                }
            }
        }
        else{
            Toast.makeText(context,"Rating is not valid", Toast.LENGTH_SHORT).show()
        }
    }
    .setNegativeButton("Cancel", null)
    .create()
}
companion object {
    const val TAG = "NewCommentDialogFragment"
}

override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
    selectedIndex = position
}

override fun onNothingSelected(parent: AdapterView<*>?) {
    selectedIndex = 0
}
}