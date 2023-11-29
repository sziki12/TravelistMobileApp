package hu.bme.aut.android.gyakorlas.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.edit
import androidx.navigation.fragment.findNavController
import hu.bme.aut.android.gyakorlas.R
import hu.bme.aut.android.gyakorlas.databinding.FragmentSignUpBinding

class SignUpFragment : Fragment() {
    private lateinit var binding : FragmentSignUpBinding
    private val sharedPreferencesName = "user_data"
    private val emailKey = "email"
    private val usernameKey = "username"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSignUpBinding.inflate(inflater, container, false)
        return binding.root;
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnSignUp.setOnClickListener {
            if (binding.etFullName.text.toString().isEmpty()){
                binding.etFullName.requestFocus()
                binding.etFullName.error = "Please enter your full name"
            }
            else if (binding.etEmail.text.toString().isEmpty()) {
                binding.etEmail.requestFocus()
                binding.etEmail.error = "Please enter your email"
            }
            else if (binding.etUsername.text.toString().isEmpty()) {
                binding.etUsername.requestFocus()
                binding.etUsername.error = "Please enter your username"
            }
            else if (binding.etPassword.text.toString().isEmpty()) {
                binding.etPassword.requestFocus()
                binding.etPassword.error = "Please enter your password"
            }
            else if (binding.etConfirmPassword.text.toString().isEmpty()) {
                binding.etConfirmPassword.requestFocus()
                binding.etConfirmPassword.error = "Please confirm your password"
            }
            else if (binding.etPassword.text.toString() != binding.etConfirmPassword.text.toString()) {
                binding.etConfirmPassword.requestFocus()
                binding.etConfirmPassword.error = "Passwords not matching!"
            }
            else {
                saveUserData()
                findNavController().navigate(R.id.action_signUpFragment_to_loginFragment)
            }
        }
        binding.imgbtnArrowBack.setOnClickListener()
        {
            findNavController().navigate(R.id.action_signUpFragment_to_loginFragment)
        }
    }

    private fun saveUserData() {
        val email = binding.etEmail.text.toString()
        val username = binding.etUsername.text.toString()

        val sharedPreferences =
            requireActivity().getSharedPreferences(sharedPreferencesName, Context.MODE_PRIVATE)

        sharedPreferences.edit {
//            putString(emailKey, email)
//            putString(usernameKey, username)
            putString(email, username)
            apply()
        }

        Toast.makeText(requireContext(), "Data saved", Toast.LENGTH_LONG).show()
    }
}