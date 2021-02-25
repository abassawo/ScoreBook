package com.lindenlabs.scorebook.androidApp.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.lindenlabs.scorebook.androidApp.data.model.Player


/**
 * Data access object to query the database.
 */
@Dao
interface PlayerStore {

    @Query("SELECT * FROM players ORDER BY id DESC")
    fun getAll(): List<Player>

    @Insert
    fun insertAll(vararg players: Player)

    @Query("DELETE FROM players")
    fun clearTable()
}
