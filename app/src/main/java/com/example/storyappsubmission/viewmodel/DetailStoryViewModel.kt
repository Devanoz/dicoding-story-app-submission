package com.example.storyappsubmission.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.storyappsubmission.api.ApiConfig
import com.example.storyappsubmission.api.ApiService
import com.example.storyappsubmission.api.pojo.DetailStoryResponse
import com.example.storyappsubmission.data.local.PreferencesDataStoreConstans
import com.example.storyappsubmission.data.local.PreferencesDataStoreHelper
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailStoryViewModel(application: Application)  : ViewModel() {
    private lateinit var token: String
    private lateinit var client : ApiService

    init {
        viewModelScope.launch {
            token = PreferencesDataStoreHelper(application).getFirstPreference(
                PreferencesDataStoreConstans.TOKEN,
                ""
            )
            client = ApiConfig.getApiServiceWithToken(token)
        }
    }

    fun getDetailViewModel(id: String) {
        val call = client.getStoryDetail(id)
        call.enqueue(object: Callback<DetailStoryResponse> {
            override fun onResponse(
                call: Call<DetailStoryResponse>,
                response: Response<DetailStoryResponse>
            ) {
               if(response.isSuccessful) {

               }
            }

            override fun onFailure(call: Call<DetailStoryResponse>, t: Throwable) {
                //make toast or snackbar
            }
        })
    }
}