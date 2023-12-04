package hu.bme.aut.android.gyakorlas.fragments

import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.SeekBar
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.maps.model.LatLng
import hu.bme.aut.android.gyakorlas.R
import hu.bme.aut.android.gyakorlas.comment.Comment
import hu.bme.aut.android.gyakorlas.comment.NewCommentDialog
import hu.bme.aut.android.gyakorlas.databinding.FragmentRequestHelpBinding
import hu.bme.aut.android.gyakorlas.location.LocationService
import hu.bme.aut.android.gyakorlas.mapData.MapMarker
import hu.bme.aut.android.gyakorlas.requestHelp.HelpMessage
import hu.bme.aut.android.gyakorlas.requestHelp.HelpMessagesAdapter
import hu.bme.aut.android.gyakorlas.requestHelp.RequestHelpDialogFragment
import hu.bme.aut.android.gyakorlas.requestHelp.RequestHelpListener
import kotlin.concurrent.thread


class RequestHelpFragment : Fragment(), RequestHelpListener {
    private lateinit var binding : FragmentRequestHelpBinding
    private lateinit var helpMessagesAdapter: HelpMessagesAdapter
    private var helpMessages:ArrayList<HelpMessage> = ArrayList()
    var markers: ArrayList<MapMarker> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRequestHelpBinding.inflate(inflater, container, false)
        return binding.root;
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        helpMessagesAdapter = HelpMessagesAdapter(helpMessages)

        binding.rvMessages.layoutManager = LinearLayoutManager(this.context)
        binding.rvMessages.adapter = helpMessagesAdapter

        val pulseAnimation = AnimationUtils.loadAnimation(this.context, R.anim.pulse_animation)
        binding.ibRequestHelp.startAnimation(pulseAnimation)

        binding.ibRequestHelp.setOnClickListener() {
            val dialogFragment = RequestHelpDialogFragment()
            dialogFragment.listener = this
            dialogFragment.show(
                this.parentFragmentManager,
                RequestHelpDialogFragment.TAG
            )
        }
        binding.imgbtnMenu.setOnClickListener {
            findNavController().navigate(R.id.action_requestHelpFragment_to_menuFragment)
        }
    }

    override fun onRequestHelp(helpMessage: HelpMessage) {
        helpMessages.add(helpMessage)

//        val selfLatLng = LocationService.currentLocation?.latitude?.let { LocationService.currentLocation?.longitude?.let { it1 ->
//            LatLng(it,
//                it1
//            )
//        } }
//
//        if (selfLatLng != null) {
//            Log.i("SELFMARKER", selfLatLng.latitude.toString() + " " + selfLatLng.longitude.toString())
//            markers.add(MapMarker(helpMessage.username, selfLatLng.latitude, selfLatLng.longitude))
//        }
//
//
//        for (m in markers)
//            Log.i("RHMARKERS", m.name)

        helpMessagesAdapter.update(helpMessages)
    }
}
