package com.capstone.plasticwise.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.capstone.plasticwise.data.remote.ResponseCraftingItem
import kotlinx.coroutines.flow.Flow

@Dao
interface CraftingDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCraft(craft: List<ResponseCraftingItem>)

    @Query("SELECT * FROM crafting_item")
    fun getAllCraft(): Flow<List<ResponseCraftingItem>>
}