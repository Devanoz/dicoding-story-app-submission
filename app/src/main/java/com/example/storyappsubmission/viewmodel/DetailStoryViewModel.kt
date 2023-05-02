package com.example.storyappsubmission.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.storyappsubmission.R
import com.example.storyappsubmission.api.ApiConfig
import com.example.storyappsubmission.api.ApiService
import com.example.storyappsubmission.api.pojo.DetailStoryItem
import com.example.storyappsubmission.api.pojo.DetailStoryResponse
import com.example.storyappsubmission.data.local.PreferencesDataStoreConstans
import com.example.storyappsubmission.data.local.PreferencesDataStoreHelper
import com.example.storyappsubmission.helper.Event
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailStoryViewModel(private val application: Application) : ViewModel() {
    private lateinit var token: String
    private lateinit var client: ApiService

    private val _detailStory = MutableLiveData<DetailStoryItem>()
    val detailStory: LiveData<DetailStoryItem> = _detailStory

    private val _message = MutableLiveData<Event<String>>(null)
    val message: LiveData<Event<String>> = _message

    init {
        viewModelScope.launch {
            token = PreferencesDataStoreHelper(application).getFirstPreference(
                PreferencesDataStoreConstans.TOKEN,
                ""
            )
            client = ApiConfig.getApiServiceWithToken(token)
        }
    }

    fun getDetailStoryById(id: String) {
        val call = client.getStoryDetail(id)
        call.enqueue(object : Callback<DetailStoryResponse> {
            override fun onResponse(
                call: Call<DetailStoryResponse>,
                response: Response<DetailStoryResponse>
            ) {
                if (response.isSuccessful) {
                    val storyResponse = response.body()?.story
                    _detailStory.value = storyResponse
                }
            }

            override fun onFailure(call: Call<DetailStoryResponse>, t: Throwable) {
                _message.value = Event(application.getString(R.string.failed_to_load_data))
            }
        })
    }
}