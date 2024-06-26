package hu.bme.aut.android.gyakorlas.fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.JsonToken
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.edit
import androidx.navigation.fragment.findNavController
import androidx.preference.PreferenceManager
import hu.bme.aut.android.gyakorlas.R
import hu.bme.aut.android.gyakorlas.databinding.FragmentLoginBinding
import hu.bme.aut.android.gyakorlas.mapData.Token
import hu.bme.aut.android.gyakorlas.retrofit.DataAccess

class LoginFragment : Fragment() {
    private lateinit var binding : FragmentLoginBinding
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var tokenSharedPreferences: SharedPreferences

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

        tokenSharedPreferences = requireActivity().getSharedPreferences("user_token", Context.MODE_PRIVATE)

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext())
        val saveUser = sharedPreferences.getBoolean("saveUser", false)

        if (saveUser) {
            val lastSavedEmail = sharedPreferences.getString("lastSavedEmail", "")
            binding.etEmail.setText(lastSavedEmail)
        }

        binding.btnLogin.setOnClickListener {
            if (binding.etEmail.text.toString().isEmpty()){
                binding.etEmail.requestFocus()
                binding.etEmail.error = "Please enter your email address"
            }
            else if (binding.etPassword.text.toString().isEmpty()) {
                binding.etPassword.requestFocus()
                binding.etPassword.error = "Please enter your password"
            }
            else {
                val email = binding.etEmail.text.toString()
                saveLastEmail(email)
                val password = binding.etPassword.text.toString()
                DataAccess.startLoginListener(DataAccess.UserServerData(email, password),::onSuccess,::onFailure, ::onUserNotExists)
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
    private fun onFailure(message:String)
    {
        Toast.makeText(requireContext(),message,Toast.LENGTH_LONG).show()
        Log.i("Retrofit","OnFailure")
    }
    private fun onSuccess(token: String)
    {
        val tokenIndex = token.indexOf("token=")
        val tokenString = token.substring(tokenIndex + "token=".length)

        val endIndex = tokenString.indexOf("; path=/;")
        val finalToken = tokenString.substring(0, endIndex)

        Log.i("TOKEN", finalToken)
        Token.token = finalToken
        findNavController().navigate(R.id.action_loginFragment_to_menuFragment)
        Log.i("Retrofit","OnSuccess")
    }

    private fun onUserNotExists()
    {
        binding.etEmail.error = "There are no registered users with this email and password"
        Log.i("Retrofit","OnUserNotExists")
    }
}