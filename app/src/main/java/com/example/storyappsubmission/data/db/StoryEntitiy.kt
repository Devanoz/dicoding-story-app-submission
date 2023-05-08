package com.example.storyappsubmission.data.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "story")
data class StoryEntitiy(
    @PrimaryKey
    val id: String,
    @ColumnInfo(name = "name")
    val name: String?,
    @ColumnInfo(name = "description")
    val description: String?,
    @ColumnInfo(name = "photo_url")
    val photoUrl: String?,
    @ColumnInfo(name = "created_at")
    val createdAt: String?,
    @ColumnInfo(name = "latitude")
    val lat: Double?,
    @ColumnInfo(name = "longitude")
    val lon: Double?
)
