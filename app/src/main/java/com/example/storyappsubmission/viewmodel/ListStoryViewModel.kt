package com.example.storyappsubmission.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.storyappsubmission.api.ApiConfig
import com.example.storyappsubmission.api.ApiService
import com.example.storyappsubmission.api.pojo.StoriesResponse
import com.example.storyappsubmission.api.pojo.StoryItem
import com.example.storyappsubmission.data.local.PreferencesDataStoreConstans
import com.example.storyappsubmission.data.local.PreferencesDataStoreHelper
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ListStoryViewModel(private val application: Application) : ViewModel() {
    private lateinit var token: String
    private lateinit var client: ApiService

    private val _storyList = MutableLiveData<List<StoryItem>>()
    val storyList: LiveData<List<StoryItem>> = _storyList


    init {
        viewModelScope.launch {
            token = PreferencesDataStoreHelper(application).getFirstPreference(
                PreferencesDataStoreConstans.TOKEN,
                ""
            )
            client = ApiConfig.getApiServiceWithToken(token)
            getAllStories()
        }
    }

    fun getAllStories() {

        val call = client.getAllStories()
        call.enqueue(object : Callback<StoriesResponse> {
            override fun onResponse(
                call: Call<StoriesResponse>,
                response: Response<StoriesResponse>
            ) {
                if (response.isSuccessful) {
                    val storyList = response.body()?.listStory
                    _storyList.value = storyList
                }
            }

            override fun onFailure(call: Call<StoriesResponse>, t: Throwable) {
                //show snackbar
            }
        })
    }
}