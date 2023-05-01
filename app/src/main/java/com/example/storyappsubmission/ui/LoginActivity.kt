package com.example.storyappsubmission.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import com.example.storyappsubmission.databinding.ActivityLoginBinding
import com.example.storyappsubmission.viewmodel.LoginViewModel
import com.example.storyappsubmission.viewmodel.MyViewModelFactory

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val viewModel = ViewModelProvider(this, MyViewModelFactory(application))[LoginViewModel::class.java]

        etEmail = binding.etEmail
        etPassword = binding.etPassword
        etEmail.addTextChangedListener {
            binding.loginButton.isEnabled = isFormValid()
        }
        etPassword.addTextChangedListener {
            binding.loginButton.isEnabled = isFormValid()
        }

        binding.tvRegister.setOnClickListener {
            val intent = Intent(this,RegisterActivity::class.java)
            startActivity(intent)
        }
        binding.loginButton.setOnClickListener {
            viewModel.login(etEmail.text.toString(), etPassword.text.toString())
        }
        viewModel.isLoginSuccess.observe(this) { isSuccess ->
            if(isSuccess) {
                startActivity(Intent(this,ListStoryActivity::class.java))
                finish()
            }
        }
        viewModel.showLinearProgress.observe(this) {condition ->
            showLinearProgressBar(condition)
        }
        viewModel.message.observe(this) {message ->
            message?.getContentIfNotHandled()?.let {
                Toast.makeText(this@LoginActivity, it, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun isFormValid(): Boolean = binding.etEmail.isValid && binding.etPassword.isValid
    private fun showLinearProgressBar(condition: Boolean){
        binding.lpgLoginStatus.isIndeterminate = condition
        binding.lpgLoginStatus.visibility = if (condition) View.VISIBLE else View.INVISIBLE
    }
}