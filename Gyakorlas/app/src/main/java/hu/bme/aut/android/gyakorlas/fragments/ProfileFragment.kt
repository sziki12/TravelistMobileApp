package hu.bme.aut.android.gyakorlas.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.edit
import androidx.navigation.fragment.findNavController
import androidx.preference.PreferenceManager
import hu.bme.aut.android.gyakorlas.R
import hu.bme.aut.android.gyakorlas.databinding.FragmentProfileBinding

class ProfileFragment : Fragment() {
    private lateinit var binding : FragmentProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root;
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val lastSavedEmailSP = PreferenceManager.getDefaultSharedPreferences(requireContext())
        val lastSavedEmail = lastSavedEmailSP.getString("lastSavedEmail", "")
        Log.i("LASTSAVEDEMAIL", lastSavedEmail.toString())

        val emailUsernameSP = requireActivity().getSharedPreferences("user_data", Context.MODE_PRIVATE)

        val username = emailUsernameSP.getString(lastSavedEmail, "")
        Log.i("USERNAME", username.toString())

        binding.etUsername.setText(username)

        binding.btnCancel.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_menuFragment)
        }

        binding.btnSave.setOnClickListener {
            emailUsernameSP.edit {
                putString(lastSavedEmail, binding.etUsername.text.toString())
                apply()
            }

            findNavController().navigate(R.id.action_profileFragment_to_menuFragment)
        }

        binding.imgbtnMenu.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_menuFragment)
        }
    }
}