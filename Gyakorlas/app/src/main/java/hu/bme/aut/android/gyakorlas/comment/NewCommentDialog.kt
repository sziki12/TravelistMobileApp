package hu.bme.aut.android.gyakorlas.comment

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import android.widget.DatePicker
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import hu.bme.aut.android.gyakorlas.databinding.DialogNewCommentBinding
import hu.bme.aut.android.gyakorlas.mapData.MapDataProvider
import java.util.Calendar
import kotlin.concurrent.thread

class NewCommentDialog(val markerID :Int,val successCallback:()->Unit) : DialogFragment(), AdapterView.OnItemSelectedListener {
    private lateinit var binding:DialogNewCommentBinding
    private var selectedIndex = 0;
    private val mapDataProvider = MapDataProvider.instance
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
    binding = DialogNewCommentBinding.inflate(LayoutInflater.from(context))

    return AlertDialog.Builder(requireContext())
    .setTitle("New Comment")
    .setView(binding.root)
    .setPositiveButton("Ok") { dialogInterface, i ->
        if(binding.editTextComment.text.isNotEmpty()&&binding.newCommentRating.rating.isFinite())
        {
            val comment = binding.editTextComment.text.toString()
            val rating = binding.newCommentRating.rating
            thread {
                val place = mapDataProvider.getMarkerByID(markerID).place
                place?.addComment(Comment("Title",comment,rating))

                Log.i("Comments","Title $comment $rating")
                Log.i("Comments","Comments: ${place?.comments?.size}")
                requireActivity().runOnUiThread()
                {
                    successCallback()
                }
            }
        }
        else{
            Toast.makeText(context,"Name is Empty, not registered", Toast.LENGTH_SHORT).show()
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