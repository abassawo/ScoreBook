package com.lindenlabs.scorebook.androidApp.data.persistence

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

/**
 * Data access object to query the database.
 */
@Dao
interface GameStore {

    @Query("SELECT * FROM games ORDER BY name DESC")
    fun getAll(): List<GameModelRaw>

    @Insert
    fun insertAll(vararg games: GameModelRaw)

    @Update
    fun update(vararg games: GameModelRaw)

    @Query("DELETE FROM games")
    fun clearTable()
}
