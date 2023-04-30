package com.example.storyappsubmission.ui

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.storyappsubmission.databinding.ActivityAddStoryBinding
import com.example.storyappsubmission.viewmodel.AddStoryViewModel
import com.example.storyappsubmission.viewmodel.MyViewModelFactory

class AddStoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddStoryBinding
    private var uriFileToUpload: Uri? = null

    private var isFromGallery = false

    private val getImageFromGallery = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if(result.resultCode == Activity.RESULT_OK) {
            val dataUri = result.data?.data
            uriFileToUpload = dataUri
            isFromGallery = true
            Glide.with(this).load(dataUri).into(binding.imvForUpload)
        }
    }
    private val getImageFromCamera = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){result ->
        if(result.resultCode == CAMERAX_REQUEST_CODE) {
            val dataUri = result.data?.data
            uriFileToUpload = dataUri
            Glide.with(this).load(dataUri).into(binding.imvForUpload)
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val viewModel = ViewModelProvider(this, MyViewModelFactory(application))[AddStoryViewModel::class.java]

        binding.btnPickFromGallery.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"
            getImageFromGallery.launch(intent)
        }
        binding.btnTakePhoto.setOnClickListener {
            getImageFromCamera.launch(Intent(this,CameraActivity::class.java))
        }
        binding.btnUpload.setOnClickListener {
            if(uriFileToUpload != null) {
                viewModel.uploadStory(
                    fileUri = uriFileToUpload!!,
                    description = binding.etStory.text.toString(),
                    isFromGallery
                )
            }else {
                Toast.makeText(this,"File is not choosen yet",Toast.LENGTH_SHORT).show()
            }
        }
    }

    companion object {
        const val CAMERAX_REQUEST_CODE = 1002
    }
}