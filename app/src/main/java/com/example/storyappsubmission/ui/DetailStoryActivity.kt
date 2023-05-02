package com.example.storyappsubmission.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.storyappsubmission.adapter.StoriesAdapter
import com.example.storyappsubmission.databinding.ActivityDetailStoryBinding
import com.example.storyappsubmission.viewmodel.DetailStoryViewModel
import com.example.storyappsubmission.viewmodel.MyViewModelFactory

class DetailStoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailStoryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val viewModel = ViewModelProvider(
            this,
            MyViewModelFactory(application)
        )[DetailStoryViewModel::class.java]
        val storyId = intent.getStringExtra(StoriesAdapter.ID_EXTRA) as String
        viewModel.detailStory.observe(this) {
            val story = it
            binding.nameTextView.text = story.name
            binding.descriptionTextView.text = story.description
            binding.circularProgressIndicator.visibility = View.INVISIBLE
            Glide.with(this).load(story.photoUrl).into(binding.storyImageView)
        }
        viewModel.message.observe(this) { message ->
            message?.getContentIfNotHandled()?.let {
                Toast.makeText(this@DetailStoryActivity, it, Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.getDetailStoryById(storyId)

    }
}