package hu.bme.aut.android.gyakorlas.fragments

import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.preference.PreferenceManager
import hu.bme.aut.android.gyakorlas.R
import hu.bme.aut.android.gyakorlas.databinding.FragmentLoginBinding

class LoginFragment : Fragment() {
    private lateinit var binding : FragmentLoginBinding
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root;
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext())
        val saveUser = sharedPreferences.getBoolean("saveUser", false)

        if (saveUser) {
            val lastSavedEmail = sharedPreferences.getString("lastSavedEmail", "")
            binding.etUsername.setText(lastSavedEmail)
        }

        binding.btnLogin.setOnClickListener {
            if (binding.etUsername.text.toString().isEmpty()){
                binding.etUsername.requestFocus()
                binding.etUsername.error = "Please enter your username"
            }
            else if (binding.etPassword.text.toString().isEmpty()) {
                binding.etPassword.requestFocus()
                binding.etPassword.error = "Please enter your password"
            }
            else {
                val email = binding.etUsername.text.toString()
                saveLastEmail(email)

                findNavController().navigate(R.id.action_loginFragment_to_menuFragment)
            }
        }

        binding.btnSignUp.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_signUpFragment)
        }
    }

    private fun saveLastEmail(email: String) {
        val editor = sharedPreferences.edit()
        editor.putString("lastSavedEmail", email)
        editor.apply()
    }
}