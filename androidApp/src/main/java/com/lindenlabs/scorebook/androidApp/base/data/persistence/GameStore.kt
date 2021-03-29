//package com.lindenlabs.scorebook.androidApp.base.data.persistence
//
//import androidx.lifecycle.LiveData
//import androidx.room.*
//import com.lindenlabs.scorebook.shared.raw.Game
//
//@Dao
//interface GameStore {
//
//    @Query("SELECT * FROM games ORDER BY dateCreated")
//    suspend fun loadAll() : List<Game>
//
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    suspend fun insert(game: Game)
//
//    @Query("SELECT * FROM games WHERE id = :id")
//    fun getGame(id: Int): LiveData<Game>
//
//    @Update
//    suspend fun update(game: Game)
//
//    @Delete
//    fun delete(vararg games: Game)
//}