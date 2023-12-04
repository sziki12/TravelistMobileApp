package hu.bme.aut.android.gyakorlas.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import hu.bme.aut.android.gyakorlas.R
import hu.bme.aut.android.gyakorlas.databinding.FragmentRequestHelpBinding
import hu.bme.aut.android.gyakorlas.mapData.UserMarker
import hu.bme.aut.android.gyakorlas.requestHelp.HelpMessage
import hu.bme.aut.android.gyakorlas.requestHelp.UserMarkerAdapter
import hu.bme.aut.android.gyakorlas.requestHelp.RequestHelpDialogFragment
import hu.bme.aut.android.gyakorlas.requestHelp.RequestHelpListener
import hu.bme.aut.android.gyakorlas.retrofit.DataAccess


class RequestHelpFragment : Fragment(), RequestHelpListener {
    private lateinit var binding : FragmentRequestHelpBinding
    private lateinit var userMarkerAdapter: UserMarkerAdapter
    /*private var helpMessages:ArrayList<HelpMessage> = ArrayList()*/
    var userMarkers: ArrayList<UserMarker> = ArrayList()

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

        //Ebbol a userMarkers listabol atteszek minden egyes markert a helpMessages listaba, de minden userMarker-nek csak a username-t es message-t tarolom el.
        /*for(u in userMarkers){
            helpMessages.add(HelpMessage(u.username, u.message))
        }

        helpMessagesAdapter = UserMarkerAdapter(helpMessages)*/

        userMarkerAdapter = UserMarkerAdapter(userMarkers)
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
    }

    override fun onRequestHelp(userMarker: UserMarker) {
        var user = DataAccess.UserMarkerServerData(userMarker.username, userMarker.latitude, userMarker.longitude, userMarker.message)
        DataAccess.startHelpMessageListener(user, ::onSuccess, ::onFailure)
        //helpMessages.add(helpMessage)

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

        //TODO EZ KELL? hogy frissitem a recyclerviewt?
        //userMarkers.add(userMarker)
        for (u in userMarkers){
            if (u.username == userMarker.username){
                u.message = userMarker.message
            }
        }
        userMarkerAdapter.update(userMarkers)
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
