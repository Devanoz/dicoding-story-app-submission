package com.example.storyappsubmission.data.db

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface StoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStory(story: List<StoryEntitiy>)

    @Query("SELECT * FROM story")
    fun getAllStory(): PagingSource<Int, StoryEntitiy>

    @Query("DELETE FROM story")
    suspend fun deleteAllStory()
}