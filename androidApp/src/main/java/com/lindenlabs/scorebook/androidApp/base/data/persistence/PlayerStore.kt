//package com.lindenlabs.scorebook.androidApp.base.data.persistence
//
//import androidx.lifecycle.LiveData
//import androidx.room.*
//import com.lindenlabs.scorebook.androidApp.base.data.raw.Player
//
//@Dao
//interface PlayerStore {
//
//    @Query("SELECT * FROM players ORDER BY dateCreated")
//    suspend fun loadAll() : List<Player>
//
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    suspend fun insert(game: Player)
//
//    @Query("SELECT * FROM players WHERE id = :id")
//    fun getPlayer(id: Int): LiveData<Player>
//
//    @Update
//    suspend fun update(game: Player)
//
//    @Delete
//    fun delete(vararg players: Player)
//}