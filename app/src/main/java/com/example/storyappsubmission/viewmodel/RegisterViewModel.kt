package com.example.storyappsubmission.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.storyappsubmission.api.ApiConfig
import com.example.storyappsubmission.api.pojo.RegisterResponse
import com.example.storyappsubmission.data.model.RegisterModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterViewModel : ViewModel() {
    private val client = ApiConfig.getApiService()

    fun register(fullName: String, email: String, password: String) {
        val call = client.registerUser(RegisterModel(fullName,email,password))
        call.enqueue(object : Callback<RegisterResponse> {
            override fun onResponse(
                call: Call<RegisterResponse>,
                response: Response<RegisterResponse>
            ) {
                if (response.isSuccessful) {
                    val resposne = response.body()
                    Log.d("registerInfo","success")
                }else {
                    Log.d("registerInfo","failed")
                }
            }

            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {

            }
        })
    }
}