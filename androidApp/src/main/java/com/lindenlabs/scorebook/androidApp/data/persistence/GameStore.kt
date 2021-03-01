package com.lindenlabs.scorebook.androidApp.data.persistence

import androidx.room.*
import com.lindenlabs.scorebook.androidApp.screens.home.data.model.Game

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