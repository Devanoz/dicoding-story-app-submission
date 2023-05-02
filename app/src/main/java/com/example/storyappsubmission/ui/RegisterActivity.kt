package com.example.storyappsubmission.ui

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.storyappsubmission.databinding.ActivityRegisterBinding
import com.example.storyappsubmission.viewmodel.MyViewModelFactory
import com.example.storyappsubmission.viewmodel.RegisterViewModel

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val viewModel = ViewModelProvider(this, MyViewModelFactory(application))[RegisterViewModel::class.java]

        viewModel.showProgressBar.observe(this) { isShowing ->
            showProgressBar(isShowing)
        }
        viewModel.message.observe(this) {message ->
            message?.getContentIfNotHandled()?.let {
                Toast.makeText(this@RegisterActivity, it, Toast.LENGTH_SHORT).show()
            }
        }
        val etFullname = binding.etFullName
        val etEmail = binding.etEmail
        val etPassword = binding.etPassword
        binding.registerButton.setOnClickListener {
            viewModel.register(
                etFullname.text.toString(),
                etEmail.text.toString(),
                etPassword.text.toString()
            )
        }
        playAnimation()

    }

    private fun playAnimation() {
        val fullNameLabel =  ObjectAnimator.ofFloat(binding.fullNameLabel, View.ALPHA, 1f).setDuration(500)
        val fullNameEditText =  ObjectAnimator.ofFloat(binding.etFullName, View.ALPHA, 1f).setDuration(500)
        val emailLabel = ObjectAnimator.ofFloat(binding.emailLabelRegister,View.ALPHA, 1f).setDuration(500)
        val emailEditText = ObjectAnimator.ofFloat(binding.etEmail, View.ALPHA, 1f).setDuration(500)
        val passwordLabel =
            ObjectAnimator.ofFloat(binding.passwordLabelRegister, View.ALPHA, 1f).setDuration(500)
        val passwordEditText =
            ObjectAnimator.ofFloat(binding.etPassword, View.ALPHA, 1f).setDuration(500)
        val registerButton =
            ObjectAnimator.ofFloat(binding.registerButton, View.ALPHA, 1f).setDuration(500)

        val fullNameSection = AnimatorSet().apply {
            playTogether(fullNameLabel,fullNameEditText)
        }

        val emailSection = AnimatorSet().apply {
            playTogether(emailLabel,emailEditText)
        }
        val passwordSection = AnimatorSet().apply {
            playTogether(passwordLabel, passwordEditText)
            play(this).before(registerButton)
        }

        val finalAnimationSet = AnimatorSet().apply {
            playSequentially(fullNameSection,emailSection,passwordSection)
        }
        finalAnimationSet.start()
    }

    private fun showProgressBar(isShowing: Boolean) {
        binding.registerProgressIndicator.visibility = if(isShowing) View.VISIBLE else View.INVISIBLE
    }
}