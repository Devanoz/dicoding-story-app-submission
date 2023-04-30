package com.example.storyappsubmission.ui

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.storyappsubmission.databinding.ActivityAddStoryBinding

class AddStoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddStoryBinding

    private val getImageFromGallery = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if(result.resultCode == Activity.RESULT_OK) {
            val dataUri = result.data?.data as Uri
            Glide.with(this).load(dataUri).into(binding.imvForUpload)
        }
    }
    private val getImageFromCamera = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){result ->
        if(result.resultCode == CAMERAX_REQUEST_CODE) {
            val dataUri = result.data?.data as Uri
            Glide.with(this).load(dataUri).into(binding.imvForUpload)
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnPickFromGallery.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"
            getImageFromGallery.launch(intent)
        }
        binding.btnTakePhoto.setOnClickListener {
            getImageFromCamera.launch(Intent(this,CameraActivity::class.java))
        }
    }

    companion object {
        const val CAMERAX_REQUEST_CODE = 1002
    }
}