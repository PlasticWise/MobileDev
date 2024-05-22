package com.example.myintermediate.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface RemoteKeysDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(remoteKey: List<RemoteKeys>)

    @Query("select * from remote_key where id = :id")
    suspend fun getRemoteKeysId(id: String): RemoteKeys

    @Query("delete from remote_key")
    suspend fun deleteRemoteKeys()
}