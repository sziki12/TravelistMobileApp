package hu.bme.aut.android.gyakorlas.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import hu.bme.aut.android.gyakorlas.R
import hu.bme.aut.android.gyakorlas.comment.CommentsAdapter
import hu.bme.aut.android.gyakorlas.comment.NewCommentDialog
import hu.bme.aut.android.gyakorlas.databinding.FragmentCommentsBinding
import hu.bme.aut.android.gyakorlas.databinding.FragmentLoginBinding
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
        binding.commentsFragmentTitle.text = marker.name
        binding.commentsRecyclerView.adapter = marker.place?.comments?.let { CommentsAdapter(it) }
        binding.commentDetailesButton.setOnClickListener()
        {
            val action =
                CommentsFragmentDirections.actionCommentsFragmentToPlaceFragment(
                    markerID
                )
            findNavController().navigate(action)
        }

        binding.newCommentButton.setOnClickListener(){
            NewCommentDialog(markerID).show(
                this.parentFragmentManager,
                NewCommentDialog.TAG
            )}
        }
    }