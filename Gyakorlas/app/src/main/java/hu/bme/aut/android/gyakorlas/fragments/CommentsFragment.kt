package hu.bme.aut.android.gyakorlas.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import hu.bme.aut.android.gyakorlas.R
import hu.bme.aut.android.gyakorlas.comment.CommentsAdapter
import hu.bme.aut.android.gyakorlas.comment.EditOrNewCommentDialog
import hu.bme.aut.android.gyakorlas.databinding.FragmentCommentsBinding
import hu.bme.aut.android.gyakorlas.getCurrentUser
import hu.bme.aut.android.gyakorlas.mapData.MapDataProvider
import hu.bme.aut.android.gyakorlas.mapData.PlaceData


class CommentsFragment : Fragment() {


    private lateinit var binding : FragmentCommentsBinding
    var markerID: Int = 0
    var place: PlaceData? = null
    private val mapDataProvider = MapDataProvider.instance
    private val args : CommentsFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCommentsBinding.inflate(inflater, container, false)
        markerID = args.markerID
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val marker = mapDataProvider.getMarkerByID(markerID)
        place = marker.place
        binding.commentsFragmentTitle.text = marker.name
        binding.commentsRecyclerView.layoutManager = LinearLayoutManager(this.activity)
        binding.commentsRecyclerView.adapter = marker.place?.comments?.let { CommentsAdapter(it) }

        refreshCommentButtons()

        binding.deleteCommentButton.setOnClickListener()
        {
            val (email,user) = requireActivity().getCurrentUser()
            place?.removeComment(email)
            onCommentsChanged()
        }
        binding.commentDetailesButton.setOnClickListener()
        {
            val action =
                CommentsFragmentDirections.actionCommentsFragmentToPlaceFragment(
                    markerID
                )
            findNavController().navigate(action)
        }

        binding.newCommentButton.setOnClickListener(){
            EditOrNewCommentDialog(markerID,::onCommentsChanged).show(
                this.parentFragmentManager,
                EditOrNewCommentDialog.TAG
            )}
        }



    private fun onCommentsChanged()
    {
        refreshRecyclerView()
        refreshCommentButtons()
    }

    private fun refreshRecyclerView()
    {
        mapDataProvider.getMarkerByID(markerID).place?.comments?.let {
            (binding.commentsRecyclerView.adapter as CommentsAdapter).update(
                it
            )
            Log.i("Comments","refreshRecyclerView()")
        }
    }

    private fun refreshCommentButtons()
    {
        val (email, user) = requireActivity().getCurrentUser()
        if(place!!.userAlreadyCommented(email))
        {
            binding.newCommentButton.setText(R.string.edit_comment)
            binding.deleteCommentButton.visibility = View.VISIBLE
        }
        else
        {
            binding.newCommentButton.setText(R.string.new_comment)
            binding.deleteCommentButton.visibility = View.INVISIBLE
        }

    }
}