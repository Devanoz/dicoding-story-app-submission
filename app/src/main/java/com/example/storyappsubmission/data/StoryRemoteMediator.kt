package com.example.storyappsubmission.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.example.storyappsubmission.api.ApiService
import com.example.storyappsubmission.api.pojo.StoriesResponse
import com.example.storyappsubmission.api.pojo.StoryItem
import com.example.storyappsubmission.data.db.StoryDatabase
import com.example.storyappsubmission.data.db.StoryEntitiy
import retrofit2.Call
import retrofit2.Response
import java.lang.Exception

@OptIn(ExperimentalPagingApi::class)
class StoryRemoteMediator(
    private val database: StoryDatabase,
    private val apiService: ApiService
): RemoteMediator<Int, StoryEntitiy>() {
    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, StoryEntitiy>
    ): MediatorResult {
        val page = INITIAL_PAGE_INDEX
        var listStory: List<StoryItem>? = null
        try {
            val requestStories = apiService.getAllStories()

            var endOfPaginationReached = false

            requestStories.enqueue(object : retrofit2.Callback<StoriesResponse>{
                override fun onResponse(
                    call: Call<StoriesResponse>,
                    response: Response<StoriesResponse>
                ) {
                    if(response.isSuccessful) {
                        listStory = response.body()?.listStory
                        endOfPaginationReached = listStory?.isEmpty() == true
                    }
                }

                override fun onFailure(call: Call<StoriesResponse>, t: Throwable) {
                    //show something
                }
            })

            database.withTransaction {
                val storyDao = database.storyDao()
                if(loadType == LoadType.REFRESH) {
                    storyDao.deleteAllStory()
                }

                val listOfStoryEntitiy: ArrayList<StoryEntitiy> = listStory?.map {
                    StoryEntitiy(
                        name = it.name,
                        description = it.description,
                        photoUrl = it.photoUrl,
                        createdAt = it.createdAt,
                        lat = it.lat,
                        lon = it.lon,
                    )
                } as ArrayList<StoryEntitiy>

                storyDao.insertStory(listOfStoryEntitiy)
            }
            return MediatorResult.Success(endOfPaginationReached)
        }catch (exception :Exception) {
            return MediatorResult.Error(exception)
        }

    }

    override suspend fun initialize(): InitializeAction {
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    private companion object {
        const val INITIAL_PAGE_INDEX = 1
    }
}