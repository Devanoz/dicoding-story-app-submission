package com.example.storyappsubmission.ui

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
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

        val viewModel =
            ViewModelProvider(this, MyViewModelFactory(application))[LoginViewModel::class.java]

        etEmail = binding.etEmail
        etPassword = binding.etPassword
        etEmail.addTextChangedListener {
            binding.loginButton.isEnabled = isFormValid()
        }
        etPassword.addTextChangedListener {
            binding.loginButton.isEnabled = isFormValid()
        }

        binding.tvRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
        binding.loginButton.setOnClickListener {
            viewModel.login(etEmail.text.toString(), etPassword.text.toString())
        }
        viewModel.isLoginSuccess.observe(this) { isSuccess ->
            if (isSuccess) {
                startActivity(Intent(this, ListStoryActivity::class.java))
                finish()
            }
        }
        viewModel.showLinearProgress.observe(this) { condition ->
            showLinearProgressBar(condition)
        }
        viewModel.message.observe(this) { message ->
            message?.getContentIfNotHandled()?.let {
                Toast.makeText(this@LoginActivity, it, Toast.LENGTH_SHORT).show()
            }
        }
        playAnimation()
    }

    private fun playAnimation() {
        val emailLabel = ObjectAnimator.ofFloat(binding.emailLabel, View.ALPHA, 1f).setDuration(250)
        val emailEditText = ObjectAnimator.ofFloat(binding.etEmail, View.ALPHA, 1f).setDuration(250)
        val passwordLabel =
            ObjectAnimator.ofFloat(binding.passwordLabel, View.ALPHA, 1f).setDuration(250)
        val passwordEditText =
            ObjectAnimator.ofFloat(binding.etPassword, View.ALPHA, 1f).setDuration(250)
        val loginButton =
            ObjectAnimator.ofFloat(binding.loginButton, View.ALPHA, 1f).setDuration(250)
        val registerSection =
            ObjectAnimator.ofFloat(binding.registerSectionLinearLayout, View.ALPHA, 1f)
                .setDuration(500)

        ObjectAnimator.ofFloat(binding.logoImageView, View.TRANSLATION_Y, 10f, 50f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val emailSection = AnimatorSet().apply {
            playTogether(emailLabel, emailEditText)
        }
        val passwordSection = AnimatorSet().apply {
            playTogether(passwordLabel, passwordEditText)
        }
        val registrationSection = AnimatorSet().apply {
            playTogether(loginButton, registerSection)
        }
        val finalAnimationSet = AnimatorSet().apply {
            playSequentially(emailSection, passwordSection, registrationSection)
        }
        finalAnimationSet.start()

    }

    private fun isFormValid(): Boolean = binding.etEmail.isValid && binding.etPassword.isValid
    private fun showLinearProgressBar(condition: Boolean) {
        binding.lpgLoginStatus.isIndeterminate = condition
        binding.lpgLoginStatus.visibility = if (condition) View.VISIBLE else View.INVISIBLE
    }
}