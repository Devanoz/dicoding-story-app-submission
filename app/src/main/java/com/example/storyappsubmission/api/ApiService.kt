package com.example.storyappsubmission.api

import com.example.storyappsubmission.api.pojo.LoginResponse
import com.example.storyappsubmission.api.pojo.RegisterResponse
import com.example.storyappsubmission.api.pojo.StoriesResponse
import com.example.storyappsubmission.data.model.LoginModel
import com.example.storyappsubmission.data.model.RegisterModel
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {
    @POST("register")
    fun registerUser(@Body registerModel: RegisterModel): Call<RegisterResponse>

    @POST("login")
    fun login(@Body loginModel: LoginModel): Call<LoginResponse>

    @GET("stories")
    fun getAllStories(): Call<StoriesResponse>
}