package hu.bme.aut.android.gyakorlas

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import hu.bme.aut.android.gyakorlas.databinding.FragmentProfileBinding
import hu.bme.aut.android.gyakorlas.databinding.FragmentRequestHelpBinding

class RequestHelpFragment : Fragment() {
    private lateinit var binding : FragmentRequestHelpBinding

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

        binding.ibRequestHelp.setOnClickListener {
            findNavController().navigate(R.id.action_requestHelpFragment_to_menuFragment)
        }
        binding.imgbtnMenu.setOnClickListener {
            findNavController().navigate(R.id.action_requestHelpFragment_to_menuFragment)
        }
    }

}