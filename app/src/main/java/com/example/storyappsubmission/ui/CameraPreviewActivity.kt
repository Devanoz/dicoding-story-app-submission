package com.example.storyappsubmission.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.storyappsubmission.R
import com.example.storyappsubmission.databinding.ActivityCameraPreviewBinding

class CameraPreviewActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCameraPreviewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCameraPreviewBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}