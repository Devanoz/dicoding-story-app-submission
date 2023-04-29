package com.example.storyappsubmission.viewmodel

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.storyappsubmission.api.ApiConfig
import com.example.storyappsubmission.api.pojo.LoginResponse
import com.example.storyappsubmission.data.local.PreferencesDataStoreConstans
import com.example.storyappsubmission.data.local.PreferencesDataStoreHelper
import com.example.storyappsubmission.data.model.LoginModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

class LoginViewModel(private val application: Application) : AndroidViewModel(application) {
    private val client = ApiConfig.getApiService()

    private val preferencesHelper = PreferencesDataStoreHelper(application)

    private val _isLoginSucces = MutableLiveData(false)
    val isLoginSuccess: LiveData<Boolean> = _isLoginSucces

    private val _showLinearProgfress = MutableLiveData(false)
    val showLinearProgress: LiveData<Boolean> = _showLinearProgfress
    fun login(email: String, password: String) {
        _showLinearProgfress.value = true
        val call = client.login(LoginModel(email,password))
        Toast.makeText(application,"still waiting to login",Toast.LENGTH_SHORT).show()
        call.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                Toast.makeText(application,"process finished",Toast.LENGTH_SHORT).show()
                if (response.isSuccessful) {
                    val loginResponse = response.body()
                    val userId = loginResponse?.loginResult?.userId
                    val name = loginResponse?.loginResult?.name
                    val token = loginResponse?.loginResult?.token

                    viewModelScope.launch(Dispatchers.IO) {
                        preferencesHelper.putPreference(PreferencesDataStoreConstans.USER_ID, userId.toString())
                        preferencesHelper.putPreference(PreferencesDataStoreConstans.NAME, name.toString())
                        preferencesHelper.putPreference(PreferencesDataStoreConstans.TOKEN, token.toString())
                    }
                    _isLoginSucces.value = true
                    _showLinearProgfress.value = false
                }else {
                    _isLoginSucces.value = false
                    _showLinearProgfress.value = false
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                throw IOException("error")
            }
        })
    }
}