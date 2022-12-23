package com.example.democlashofbattle.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.democlashofbattle.models.Player

@Dao
interface PlayerDao {

    @Insert
    suspend fun insertAll(list: List<Player>)

    @Update
    suspend fun update(player: Player)

    @Query("SELECT * FROM Player")
    fun watchAll(): LiveData<List<Player>>

    @Query("SELECT * FROM Player WHERE remoteId = :remoteId")
    fun watchPlayer(remoteId: String): LiveData<Player>

    @Query("SELECT * FROM Player WHERE id = :id")
    suspend fun get(id: Long) : Player

    @Query("SELECT * FROM Player WHERE remoteId = :remoteId")
    suspend fun getByRemoteId(remoteId: String) : Player

    @Query("DELETE FROM Player")
    suspend fun clear()

    @Transaction
    suspend fun replaceAll(list: List<Player>) {
        clear()
        insertAll(list)
    }
}