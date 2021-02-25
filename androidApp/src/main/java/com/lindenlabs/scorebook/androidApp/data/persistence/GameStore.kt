package com.lindenlabs.scorebook.androidApp.data.persistence

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

/**
 * Data access object to query the database.
 */
@Dao
interface GameStore {

    @Query("SELECT * FROM games ORDER BY id DESC")
    fun getAll(): List<GameModelRaw>

    @Insert
    fun insertAll(vararg games: GameModelRaw)

    @Query("DELETE FROM games")
    fun clearTable()
}
