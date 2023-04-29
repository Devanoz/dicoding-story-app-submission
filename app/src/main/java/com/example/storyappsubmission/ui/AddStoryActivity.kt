package com.example.storyappsubmission.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.storyappsubmission.databinding.ActivityAddStoryBinding

class AddStoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddStoryBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}