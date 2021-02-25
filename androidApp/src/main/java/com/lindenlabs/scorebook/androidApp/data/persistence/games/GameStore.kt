package com.lindenlabs.scorebook.androidApp.data.persistence.games

import androidx.room.*
import com.lindenlabs.scorebook.androidApp.data.persistence.games.GameModelRaw

/**
 * Data access object to query the database.
 */
@Dao
interface GameStore {

    @Query("SELECT * FROM games ORDER BY id DESC")
    fun getAll(): List<GameModelRaw>

    @Insert
    fun insert(game: GameModelRaw)

    @Insert
    fun insertAll(vararg games: GameModelRaw)

    @Update
    fun updateGames(game: GameModelRaw)

    @Update
    fun updateGames(vararg games: GameModelRaw)

    @Delete
    fun delete(vararg games: GameModelRaw)

    @Query("DELETE FROM games")
    fun clearTable()
}
