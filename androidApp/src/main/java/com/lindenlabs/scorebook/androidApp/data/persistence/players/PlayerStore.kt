package com.lindenlabs.scorebook.androidApp.data.persistence.players

import androidx.room.*
import com.lindenlabs.scorebook.androidApp.data.model.Player

@Dao
interface PlayerStore {

    @Query("SELECT * FROM players ORDER BY id DESC")
    fun getAll(): List<Player>

    @Insert
    fun insert(player: Player)

    @Insert
    fun insertAll(vararg players: Player)

    @Update
    fun updatePlayer(player: Player)

    @Update
    fun updatePlayer(vararg players: Player)

    @Delete
    fun delete(vararg players: Player)

    @Query("DELETE FROM players")
    fun clearTable()
}
