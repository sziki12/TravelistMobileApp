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
import hu.bme.aut.android.gyakorlas.mapData.UserMarker
import hu.bme.aut.android.gyakorlas.requestHelp.HelpMessage
import hu.bme.aut.android.gyakorlas.requestHelp.UserMarkerAdapter
import hu.bme.aut.android.gyakorlas.requestHelp.RequestHelpDialogFragment
import hu.bme.aut.android.gyakorlas.requestHelp.RequestHelpListener
import hu.bme.aut.android.gyakorlas.retrofit.DataAccess


class RequestHelpFragment : Fragment(), RequestHelpListener {
    private lateinit var binding : FragmentRequestHelpBinding
    private lateinit var userMarkerAdapter: UserMarkerAdapter
    private var userMarkers: ArrayList<UserMarker> = ArrayList()

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

        //Lekerem a UserMarker-eket tartalmazo listat
        DataAccess.getUserMarkers()
        {
                outMarkers->
            if(outMarkers!=null)
            {
                userMarkers.clear()
                userMarkers.addAll(outMarkers)
            }
        }

        var requestedHelpUsers : ArrayList<UserMarker> = ArrayList()
        for (user in userMarkers){
            if (user.message != ""){
                requestedHelpUsers.add(user)
            }
        }

        userMarkerAdapter = UserMarkerAdapter(requestedHelpUsers)
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
            findNavController().navigate(R.id.action_requestHelpFragment_to_menuFragment)
        }

        binding.btnGotHelp.setOnClickListener {
            val (email,username) = requireActivity().getCurrentUser()
            Log.i("EMAIL", email)
            Log.i("USERNAME", username)

            var lat = LocationService.currentLocation?.latitude
            var lng = LocationService.currentLocation?.longitude
            if (lat != null && lng != null) {
                var user = DataAccess.UserMarkerServerData(lat, lng, "")
                DataAccess.startHelpMessageListener(user, ::onSuccess, ::onFailure)
            }

            //TODO EZ KELL?
            for (u in userMarkers){
                if (u.username == username){
                    u.message = ""
                }
            }

            userMarkerAdapter.update(userMarkers)
            binding.btnGotHelp.isVisible = false
        }
    }

    override fun onRequestHelp(userMarker: UserMarker) {
        var user = DataAccess.UserMarkerServerData(userMarker.latitude, userMarker.longitude, userMarker.message)
        DataAccess.startHelpMessageListener(user, ::onSuccess, ::onFailure)

        //TODO EZ KELL? hogy frissitem a recyclerviewt?
        //az add elvileg nem kell!!!
        userMarkers.add(userMarker)
        Log.i("USERMARKER", userMarkers.size.toString())
        for (u in userMarkers){
            if (u.username == userMarker.username){
                u.message = userMarker.message
            }
        }
        userMarkerAdapter.update(userMarkers)

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
}
