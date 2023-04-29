package com.example.storyappsubmission.ui

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.storyappsubmission.R
import com.example.storyappsubmission.adapter.StoriesAdapter
import com.example.storyappsubmission.data.local.PreferencesDataStoreConstans
import com.example.storyappsubmission.data.local.PreferencesDataStoreHelper
import com.example.storyappsubmission.databinding.ActivityListStoryBinding
import com.example.storyappsubmission.viewmodel.ListStoryViewModel
import com.example.storyappsubmission.viewmodel.MyViewModelFactory
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ListStoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityListStoryBinding
    private lateinit var preferencesHelper: PreferencesDataStoreHelper
    private lateinit var storiesAdapter: StoriesAdapter
    private lateinit var toolbarStory: MaterialToolbar
    private lateinit var fabAddStory: ExtendedFloatingActionButton
    private lateinit var srlRefreshStory: SwipeRefreshLayout

    private lateinit var name: String
    private lateinit var userId: String
    private lateinit var token: String
    override fun onCreate(savedInstanceState: Bundle?) {
        // Handle the splash screen transition.
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        binding = ActivityListStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        splashScreen.setKeepOnScreenCondition{ true }
        preferencesHelper = PreferencesDataStoreHelper(this.applicationContext)
        srlRefreshStory = binding.srlRefreshStory

        toolbarStory = binding.toolbarStory
        setSupportActionBar(toolbarStory)
        fabAddStory = binding.fabAddStory

        val rvStories = binding.rvStory
        val viewModel = ViewModelProvider(this, MyViewModelFactory(application))[ListStoryViewModel::class.java]

        viewModel.storyList.observe(this) { storyList ->
            rvStories.layoutManager = LinearLayoutManager(this)
            storiesAdapter = StoriesAdapter(storyList)
            rvStories.adapter = storiesAdapter
            srlRefreshStory.isRefreshing = false
        }

        lifecycleScope.launch {
            getUserName()
            if (name.isEmpty()) {
                startActivity(Intent(this@ListStoryActivity, LoginActivity::class.java))
                finish()
                splashScreen.setKeepOnScreenCondition{ false }
            }else {
                withContext(Dispatchers.Main) {
//                    binding.userName.text = name
                }
                splashScreen.setKeepOnScreenCondition{ false }
            }
        }
        toolbarStory.setOnMenuItemClickListener {
            when(it.itemId) {
                R.id.logout -> {
                    lifecycleScope.launch {
                        logout()
                        startActivity(Intent(this@ListStoryActivity,LoginActivity::class.java))
                        finish()
                    }
                    true
                }
                else -> false
            }
        }
        srlRefreshStory.setOnRefreshListener { viewModel.getAllStories() }
        fabAddStory.setOnClickListener {
            val intent = Intent(this, AddStoryActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.story_menu, menu)
        return true
    }

    private suspend fun getUserName() {
        withContext(Dispatchers.IO) {
            name = preferencesHelper.getFirstPreference(PreferencesDataStoreConstans.NAME, "")
            userId = preferencesHelper.getFirstPreference(PreferencesDataStoreConstans.USER_ID, "")
            token = preferencesHelper.getFirstPreference(PreferencesDataStoreConstans.TOKEN, "")
        }
    }

    private suspend fun logout() {
        withContext(Dispatchers.IO) {
            preferencesHelper.clearAllPreference()
        }
    }

}