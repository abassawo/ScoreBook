package com.lindenlabs.scorebook.androidApp.base.data.persistence

import androidx.room.*
import com.lindenlabs.scorebook.androidApp.base.data.raw.Game

@Dao
interface GameStore {

    @Query("SELECT * FROM games")
    fun loadAll() : List<Game>

    @Insert
    fun insert(game: Game)

    @Update
    fun update(game: Game)

    @Delete
    fun delete(vararg games: Game)
}