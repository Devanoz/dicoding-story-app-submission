package com.example.storyappsubmission.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import com.example.storyappsubmission.data.local.PreferencesDataStoreConstans
import com.example.storyappsubmission.data.local.PreferencesDataStoreHelper
import com.example.storyappsubmission.databinding.ActivityListStoryBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ListStoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityListStoryBinding
    private lateinit var preferencesHelper: PreferencesDataStoreHelper

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
        lifecycleScope.launch {
            getUserName()
            if (name.isEmpty()) {
                startActivity(Intent(this@ListStoryActivity, LoginActivity::class.java))
                finish()
                splashScreen.setKeepOnScreenCondition{ false }
            }else {
                withContext(Dispatchers.Main) {
                    binding.userName.text = name
                }
                splashScreen.setKeepOnScreenCondition{ false }
            }
        }
        binding.btLogout.setOnClickListener {
            lifecycleScope.launch {
                logout()
                startActivity(Intent(this@ListStoryActivity,LoginActivity::class.java))
                finish()
            }
        }
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