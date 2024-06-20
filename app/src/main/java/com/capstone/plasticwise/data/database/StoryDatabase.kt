package com.capstone.plasticwise.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.capstone.plasticwise.data.remote.ResponseCraftingItem
import com.capstone.plasticwise.utils.Converters

@Database(
    entities = [ResponseCraftingItem::class],
    version = 3,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class StoryDatabase : RoomDatabase() {

    abstract fun craftingDao(): CraftingDao

    companion object {
        @Volatile
        private var INSTANCE: StoryDatabase? = null

        @JvmStatic
        fun getDatabase(context: Context): StoryDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    StoryDatabase::class.java, "story_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { INSTANCE = it }
            }
        }
    }

}