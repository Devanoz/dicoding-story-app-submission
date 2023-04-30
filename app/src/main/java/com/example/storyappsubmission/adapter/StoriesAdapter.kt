package com.example.storyappsubmission.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.storyappsubmission.api.pojo.StoryItem
import com.example.storyappsubmission.databinding.ItemStoryBinding
import com.example.storyappsubmission.ui.DetailStoryActivity

class StoriesAdapter(private val storyList: List<StoryItem>) :
    RecyclerView.Adapter<StoriesAdapter.ViewHolder>() {

    var activityContext : Context? = null

    inner class ViewHolder(private val binding: ItemStoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(storyItem: StoryItem) {
            val itemContext = binding.root.context
            Glide.with(itemContext).load(storyItem.photoUrl).into(binding.imvStory)
            binding.tvName.text = storyItem.name
            binding.tvDescription.text = storyItem.description
            binding.tvDate.text = storyItem.createdAt
            binding.detailButton.setOnClickListener {
                val intent = Intent(activityContext, DetailStoryActivity::class.java)
                intent.putExtra(ID_EXTRA,storyItem.id)
                activityContext?.startActivity(intent)
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ItemStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = storyList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(storyList[position])
    }

    companion object {
        const val ID_EXTRA = "id_extra"
    }
}