package hu.bme.aut.android.gyakorlas

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import hu.bme.aut.android.gyakorlas.databinding.ActivityMainBinding
import hu.bme.aut.android.gyakorlas.databinding.ActivitySignUpBinding

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding : ActivitySignUpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnSignUp.setOnClickListener()
        {
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
                startActivity(Intent(this, MainActivity::class.java))
            }
        }

        binding.imgbtnArrowBack.setOnClickListener()
        {
            var intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}