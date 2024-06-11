package com.capstone.plasticwise.data.database

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.capstone.plasticwise.data.remote.ListStoryItem


@Dao
interface StoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStory(story: List<ListStoryItem>)

    @Query("select * from story")
    fun getAllStory(): PagingSource<Int, ListStoryItem>

    @Query("delete from story")
    suspend fun deleteAll()

}