package com.example.storyappsubmission.viewmodel

import android.app.Application
import android.content.ContentResolver
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.storyappsubmission.api.ApiConfig
import com.example.storyappsubmission.api.ApiService
import com.example.storyappsubmission.api.pojo.AddStoryResponse
import com.example.storyappsubmission.data.local.PreferencesDataStoreConstans
import com.example.storyappsubmission.data.local.PreferencesDataStoreHelper
import kotlinx.coroutines.launch
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.Locale


class AddStoryViewModel(private val application: Application) : ViewModel() {
    private lateinit var token: String
    private lateinit var client : ApiService

    private val timeStamp: String = SimpleDateFormat(
        FILENAME_FORMAT,
        Locale.US
    ).format(System.currentTimeMillis())

    private val _isUploadSuccess = MutableLiveData(false)
    val isUploadSuccess: LiveData<Boolean> = _isUploadSuccess

    init {
        viewModelScope.launch {
            token = PreferencesDataStoreHelper(application).getFirstPreference(
                PreferencesDataStoreConstans.TOKEN,
                ""
            )
            client = ApiConfig.getApiServiceWithToken(token)
        }
    }

    fun uploadStory(fileUri: Uri, description: String, isFromGallery: Boolean) {
        var file = uriToFile(fileUri,application)
        if(!isFromGallery) {
            rotateFile(file, isBackCamera = true)
        }
        file = reduceFileImage(file)
        val requestImageFile = RequestBody.create(MediaType.parse("image/*"),file)
        val filePart = MultipartBody.Part.createFormData("photo",file.name,requestImageFile)
        val descriptionBody = RequestBody.create(MediaType.parse("text/plain"), description)
        val call = client.postStory(filePart,descriptionBody)
        Log.d("fasdf","uploading")
        call.enqueue(object: Callback<AddStoryResponse>{
            override fun onResponse(
                call: Call<AddStoryResponse>,
                response: Response<AddStoryResponse>
            ) {
                if(response.isSuccessful) {
                    _isUploadSuccess.value = true
                    Toast.makeText(application, "upload success",Toast.LENGTH_SHORT).show()
                }else {
                    Toast.makeText(application,"failed",Toast.LENGTH_SHORT).show()
                }
                Log.d("fasdf",response.message())

            }
            override fun onFailure(call: Call<AddStoryResponse>, t: Throwable) {
                Log.d("fasdf",t.message.toString())
                Toast.makeText(application,"faileds",Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun rotateFile(file: File, isBackCamera: Boolean = false) {
        val matrix = Matrix()
        val bitmap = BitmapFactory.decodeFile(file.path)
        val rotation = if (isBackCamera) 90f else -90f
        matrix.postRotate(rotation)
        if (!isBackCamera) {
            matrix.postScale(-1f, 1f, bitmap.width / 2f, bitmap.height / 2f)
        }
        val result = Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
        result.compress(Bitmap.CompressFormat.JPEG, 100, FileOutputStream(file))
    }

    private fun uriToFile(selectedImg: Uri, application: Application): File {
        val contentResolver: ContentResolver = application.contentResolver
        val myFile = createCustomTempFile(application)

        val inputStream = contentResolver.openInputStream(selectedImg) as InputStream
        val outputStream: OutputStream = FileOutputStream(myFile)
        val buf = ByteArray(1024)
        var len: Int
        while (inputStream.read(buf).also { len = it } > 0) outputStream.write(buf, 0, len)
        outputStream.close()
        inputStream.close()

        return myFile
    }

    private fun createCustomTempFile(application: Application): File {
        val storageDir: File? = application.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(timeStamp, ".jpg", storageDir)
    }

    private fun reduceFileImage(file: File): File {
        val bitmap = BitmapFactory.decodeFile(file.path)
        var compressQuality = 100
        var streamLength: Int
        do {
            val bmpStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, compressQuality, bmpStream)
            val bmpPicByteArray = bmpStream.toByteArray()
            streamLength = bmpPicByteArray.size
            compressQuality -= 5
        } while (streamLength > MAX_IMAGE_SIZE)
        bitmap.compress(Bitmap.CompressFormat.JPEG, compressQuality, FileOutputStream(file))
        return file
    }
    companion object {
        private const val MAX_IMAGE_SIZE = 1000000
        private const val FILENAME_FORMAT = "dd-MMM-yyyy"
        private const val MAXIMAL_SIZE = 1000000
    }


}