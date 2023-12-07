package hu.bme.aut.android.gyakorlas.fragments

import android.content.Context
import android.content.SharedPreferences
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
import hu.bme.aut.android.gyakorlas.mapData.TrackOthersDataProvider
import hu.bme.aut.android.gyakorlas.mapData.UserMarker
import hu.bme.aut.android.gyakorlas.requestHelp.HelpMessage
import hu.bme.aut.android.gyakorlas.requestHelp.UserMarkerAdapter
import hu.bme.aut.android.gyakorlas.requestHelp.RequestHelpDialogFragment
import hu.bme.aut.android.gyakorlas.requestHelp.RequestHelpListener
import hu.bme.aut.android.gyakorlas.retrofit.DataAccess


class RequestHelpFragment : Fragment(), RequestHelpListener, TrackOthersDataProvider.TrackOthersDataChangedListener {
    private lateinit var binding : FragmentRequestHelpBinding
    private lateinit var userMarkerAdapter: UserMarkerAdapter
    private var userMarkers: ArrayList<UserMarker> = ArrayList()
    private lateinit var tokenSharedPreferences: SharedPreferences
    private lateinit var token: String
    private val trackOthersDataProvider = TrackOthersDataProvider.instance

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

        tokenSharedPreferences = requireActivity().getSharedPreferences("user_token", Context.MODE_PRIVATE)
        token = tokenSharedPreferences.getString("token", "")?:""

        //Lekerem a UserMarker-eket tartalmazo listat
        /*DataAccess.getUserMarkers()
        {
                outMarkers->
            if(outMarkers!=null)
            {
                userMarkers.clear()
                userMarkers.addAll(outMarkers)
            }
        }*/
        Log.i("REQUESTMARKERSIZE", userMarkers.size.toString())

        userMarkerAdapter = UserMarkerAdapter(getRequestedHelpUsers(trackOthersDataProvider.markers))
        binding.rvMessages.layoutManager = LinearLayoutManager(this.context)
        binding.rvMessages.adapter = userMarkerAdapter

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
                var user = DataAccess.UserMarkerServerData(token, lat, lng, "")
                DataAccess.startHelpMessageListener(user, ::onSuccess, ::onFailure)

                //TODO ne lat, lng-gal azonositsuk a usert, hanem id/username
                for (m in trackOthersDataProvider.markers){
                    if (m.latitude == user.latitude && m.longitude == user.longitude){
                        m.message = user.message
                    }
                }
            }

            activity?.runOnUiThread {
                userMarkerAdapter.update(getRequestedHelpUsers(trackOthersDataProvider.markers))
            }

            binding.btnGotHelp.isVisible = false
        }
    }

    override fun onRequestHelp(userMarker: UserMarker) {
        var user = DataAccess.UserMarkerServerData(token, userMarker.latitude, userMarker.longitude, userMarker.message)
        DataAccess.startHelpMessageListener(user, ::onSuccess, ::onFailure)

        //TODO ne lat, lng-gal azonositsuk a usert, hanem id/username
        for (m in trackOthersDataProvider.markers){
            if (m.latitude == userMarker.latitude && m.longitude == userMarker.longitude){
                m.message = userMarker.message
            }
        }

        activity?.runOnUiThread {
            userMarkerAdapter.update(getRequestedHelpUsers(trackOthersDataProvider.markers))
        }

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
