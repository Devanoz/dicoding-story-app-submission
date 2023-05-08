package com.example.storyappsubmission.data.db

import androidx.lifecycle.LiveData
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.storyappsubmission.api.ApiService


class StoryRepository(private val storyDatabase: StoryDatabase, apiService: ApiService) {
    private val storyDao = storyDatabase.storyDao()

    suspend fun insertStory(storyList: List<StoryEntitiy>) = storyDao.insertStory(storyList)

    fun getAllStory() = storyDao.getAllStory()

    suspend fun deleteAllStory() = storyDao.deleteAllStory()

//    @OptIn(ExperimentalPagingApi::class)
//    fun getAllStories(): LiveData<PagingData<StoryEntitiy>> {
//        return Pager(
//            config = PagingConfig(
//                pageSize = 5
//            ),
//        )
//    }
}