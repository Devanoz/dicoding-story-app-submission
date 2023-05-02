package com.example.storyappsubmission.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.storyappsubmission.R
import com.example.storyappsubmission.api.ApiConfig
import com.example.storyappsubmission.api.pojo.RegisterResponse
import com.example.storyappsubmission.data.model.RegisterModel
import com.example.storyappsubmission.helper.Event
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterViewModel(private val application: Application) : ViewModel() {
    private val client = ApiConfig.getApiService()

    private val _showProgressBar = MutableLiveData(false)
    val showProgressBar: LiveData<Boolean> = _showProgressBar

    private val _message = MutableLiveData<Event<String>>(null)
    val message: LiveData<Event<String>> = _message

    fun register(fullName: String, email: String, password: String) {
        _showProgressBar.value = true
        val call = client.registerUser(RegisterModel(fullName, email, password))
        call.enqueue(object : Callback<RegisterResponse> {
            override fun onResponse(
                call: Call<RegisterResponse>,
                response: Response<RegisterResponse>
            ) {
                if (response.isSuccessful) {
                    _showProgressBar.value = false
                    _message.value = Event(application.getString(R.string.register_success))
                } else {
                }
            }

            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                _message.value = Event(application.getString(R.string.failed_to_register_try_again))
            }
        })
    }
}