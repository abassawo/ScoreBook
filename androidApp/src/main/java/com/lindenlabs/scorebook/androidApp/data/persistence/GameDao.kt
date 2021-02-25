package com.lindenlabs.scorebook.androidApp.data.persistence

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.lindenlabs.scorebook.androidApp.screens.home.data.model.Game

/**
 * Data access object to query the database.
 */
@Dao
interface GameDao {

    @Query("SELECT * FROM games ORDER BY id DESC")
    fun getAll(): List<Game>

    @Insert
    fun insertAll(vararg games: Game)

    @Query("DELETE FROM games")
    fun clearTable()
}
