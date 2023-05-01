package com.example.storyappsubmission.ui

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.storyappsubmission.databinding.ActivityRegisterBinding
import com.example.storyappsubmission.viewmodel.RegisterViewModel

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var viewModel: ViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this)[RegisterViewModel::class.java]

        (viewModel as RegisterViewModel).showProgressBar.observe(this) { isShowing ->
            showProgressBar(isShowing)
        }
        (viewModel as RegisterViewModel).message.observe(this) {message ->
            message?.getContentIfNotHandled().let {
                Toast.makeText(this@RegisterActivity, it, Toast.LENGTH_SHORT).show()
            }
        }
        val etFullname = binding.etFullName
        val etEmail = binding.etEmail
        val etPassword = binding.etPassword
        binding.registerButton.setOnClickListener {
            (viewModel as RegisterViewModel).register(
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