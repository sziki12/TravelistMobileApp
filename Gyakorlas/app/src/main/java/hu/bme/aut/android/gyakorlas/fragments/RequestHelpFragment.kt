package hu.bme.aut.android.gyakorlas.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import hu.bme.aut.android.gyakorlas.R
import hu.bme.aut.android.gyakorlas.databinding.FragmentRequestHelpBinding
import hu.bme.aut.android.gyakorlas.getCurrentUser
import hu.bme.aut.android.gyakorlas.location.LocationService
import hu.bme.aut.android.gyakorlas.mapData.Token
import hu.bme.aut.android.gyakorlas.mapData.TrackOthersDataProvider
import hu.bme.aut.android.gyakorlas.mapData.UserMarker
import hu.bme.aut.android.gyakorlas.requestHelp.UserMarkerAdapter
import hu.bme.aut.android.gyakorlas.requestHelp.RequestHelpDialogFragment
import hu.bme.aut.android.gyakorlas.requestHelp.RequestHelpListener
import hu.bme.aut.android.gyakorlas.retrofit.DataAccess


class RequestHelpFragment : Fragment(), RequestHelpListener, TrackOthersDataProvider.TrackOthersDataChangedListener {
    private lateinit var binding : FragmentRequestHelpBinding
    private lateinit var userMarkerAdapter: UserMarkerAdapter
    private var userMarkers: ArrayList<UserMarker> = ArrayList()
    private val trackOthersDataProvider = TrackOthersDataProvider.instance

    companion object {
        var requesetedHelp: Boolean = false
    }

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

        trackOthersDataProvider.addListener(this)

        userMarkerAdapter = UserMarkerAdapter(getRequestedHelpUsers(trackOthersDataProvider.markers))
        binding.rvMessages.layoutManager = LinearLayoutManager(this.context)
        binding.rvMessages.adapter = userMarkerAdapter

        binding.btnGotHelp.isVisible = requesetedHelp

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
            try{
                findNavController().navigate(R.id.action_requestHelpFragment_to_menuFragment)
            }
            catch(e: Exception){
                Log.i("REQEX", e.message.toString())
            }
        }

        binding.btnGotHelp.setOnClickListener {
            val (email,username) = requireActivity().getCurrentUser()
            Log.i("EMAIL", email)
            Log.i("USERNAME", username)

            var lat = LocationService.currentLocation?.latitude
            var lng = LocationService.currentLocation?.longitude
            if (lat != null && lng != null) {
                var user = DataAccess.UserMarkerServerData(Token.token, lat, lng, "solved")
                DataAccess.startHelpMessageListener(user, ::onSuccess, ::onFailure)
            }

            binding.btnGotHelp.isVisible = false

            requesetedHelp = false
        }
    }

    override fun onRequestHelp(userMarker: UserMarker) {
        var user = DataAccess.UserMarkerServerData(Token.token, userMarker.latitude, userMarker.longitude, userMarker.message)
        DataAccess.startHelpMessageListener(user, ::onSuccess, ::onFailure)

        requesetedHelp = true

        //Beallitod, hogy az "I got help" gomb lathato legyen
        binding.btnGotHelp.isVisible = true
    }

    private fun onFailure(message:String)
    {
        Toast.makeText(requireContext(),message, Toast.LENGTH_LONG).show()
        Log.i("Retrofit","OnFailure")
    }
    private fun onSuccess()
    {
        Log.i("Retrofit","OnSuccess")
    }

    override fun onTrackOthersDataChanged(markers: List<UserMarker>) {
        activity?.runOnUiThread {
            userMarkerAdapter.update(getRequestedHelpUsers(markers))
        }
    }

    private fun getRequestedHelpUsers(markers: List<UserMarker>) : ArrayList<UserMarker> {
        var requestedHelpUsers : ArrayList<UserMarker> = ArrayList()
        for (user in markers){
            if (user.message != ""){
                requestedHelpUsers.add(user)
                Log.i("REQUESTEDHELP", user.message)
            }
        }
        return requestedHelpUsers
    }

    override fun onDestroy() {
        super.onDestroy()
        trackOthersDataProvider.removeListener(this)
    }
}
