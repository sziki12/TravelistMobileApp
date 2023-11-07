package hu.bme.aut.android.gyakorlas.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import hu.bme.aut.android.gyakorlas.R
import hu.bme.aut.android.gyakorlas.databinding.FragmentMenuBinding

class MenuFragment : Fragment() {
    private lateinit var binding : FragmentMenuBinding


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMenuBinding.inflate(inflater, container, false)
        return binding.root;
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnMenuProfile.setOnClickListener {
            findNavController().navigate(R.id.action_menuFragment_to_profileFragment)
        }
        binding.btnMenuSearch.setOnClickListener {
            findNavController().navigate(R.id.action_menuFragment_to_mapsFragment)
        }
        binding.btnTrackOthers.setOnClickListener {
            findNavController().navigate(R.id.action_menuFragment_to_trackOthersFragment)
        }
        binding.btnMenuRecommended.setOnClickListener {
            findNavController().navigate(R.id.action_menuFragment_to_recommendedFragment)
        }
        binding.btnUploadNewPlace.setOnClickListener {
            findNavController().navigate(R.id.action_menuFragment_to_uploadNewPlaceFragment)
        }

        binding.btnRequestHelp.setOnClickListener {
            findNavController().navigate(R.id.action_menuFragment_to_requestHelpFragment)
        }

        binding.btnSettings.setOnClickListener() {
            findNavController().navigate(R.id.action_menuFragment_to_settingsFragment)

        }
    }



}