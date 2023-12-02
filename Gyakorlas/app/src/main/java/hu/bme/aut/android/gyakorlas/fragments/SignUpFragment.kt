package hu.bme.aut.android.gyakorlas.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.edit
import androidx.navigation.fragment.findNavController
import hu.bme.aut.android.gyakorlas.R
import hu.bme.aut.android.gyakorlas.databinding.FragmentSignUpBinding
import hu.bme.aut.android.gyakorlas.retrofit.DataAccess

class SignUpFragment : Fragment() {
    private lateinit var binding : FragmentSignUpBinding
    private val sharedPreferencesName = "user_data"

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
                val email = binding.etEmail.text.toString()
                val password = binding.etPassword.text.toString()
                val user = DataAccess.UserData(email,password)
                DataAccess.startRegistrationListener(user,::onSuccess,::onFailure,::onUserExists)
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
            putString(email, username)
            apply()
        }
    }
    private fun onFailure(message:String)
    {
        Toast.makeText(requireContext(),message, Toast.LENGTH_LONG).show()
        Log.i("Retrofit","OnFailure")
    }
    private fun onSuccess()
    {
        findNavController().navigate(R.id.action_signUpFragment_to_loginFragment)
        Log.i("Retrofit","OnSuccess")
    }

    private fun onUserExists()
    {
        binding.etUsername.error = "There is already a registered user with this email and password"
        Log.i("Retrofit","OnUserNotExists")
    }
}