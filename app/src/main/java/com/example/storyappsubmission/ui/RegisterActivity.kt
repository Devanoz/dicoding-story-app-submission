package com.example.storyappsubmission.ui

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.storyappsubmission.databinding.ActivityRegisterBinding
import com.example.storyappsubmission.viewmodel.LoginViewModel
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
            message?.getContentIfNotHandled().let {
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


    }
    private fun showProgressBar(isShowing: Boolean) {
        binding.registerProgressIndicator.visibility = if(isShowing) View.VISIBLE else View.INVISIBLE
    }
}