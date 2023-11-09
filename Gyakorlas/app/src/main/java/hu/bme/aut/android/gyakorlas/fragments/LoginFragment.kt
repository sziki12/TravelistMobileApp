package hu.bme.aut.android.gyakorlas.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import hu.bme.aut.android.gyakorlas.R
import hu.bme.aut.android.gyakorlas.databinding.FragmentLoginBinding
import hu.bme.aut.android.gyakorlas.retrofit.DataAccess
import hu.bme.aut.android.gyakorlas.retrofit.UserData

class LoginFragment : Fragment() {
    private lateinit var binding : FragmentLoginBinding

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
                val username = binding.etUsername.text.toString()
                val password = binding.etPassword.text.toString()
                DataAccess.registerLoginListener(UserData(username,password),::onSuccess,::onFailure, ::onUserExists)
            }
        }

        binding.btnSignUp.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_signUpFragment)
        }
    }

    private fun onFailure(message:String)
    {
        Toast.makeText(requireContext(),message,Toast.LENGTH_LONG).show()
        Log.i("Retrofit","OnFailure")
    }
    private fun onSuccess()
    {
        findNavController().navigate(R.id.action_loginFragment_to_menuFragment)
        Log.i("Retrofit","OnSuccess")
    }

    private fun onUserExists()
    {
        binding.etUsername.error = "There are no registered users with this email and password"
        Log.i("Retrofit","OnUserNotExists")
    }
}