package com.example.storyappsubmission.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.example.storyappsubmission.R
import com.example.storyappsubmission.databinding.ActivityDetailStoryBinding
import com.example.storyappsubmission.viewmodel.DetailStoryViewModel
import com.example.storyappsubmission.viewmodel.ListStoryViewModel
import com.example.storyappsubmission.viewmodel.MyViewModelFactory

class DetailStoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailStoryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding  = ActivityDetailStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val viewModel = ViewModelProvider(this, MyViewModelFactory(application))[DetailStoryViewModel::class.java]


    }
}