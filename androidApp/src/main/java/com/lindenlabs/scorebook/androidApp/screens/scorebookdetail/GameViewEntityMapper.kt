package com.lindenlabs.scorebook.androidApp.screens.scorebookdetail

import com.lindenlabs.scorebook.androidApp.screens.scorebookdetail.entities.ScoreBookEntity
import com.lindenlabs.scorebook.androidApp.screens.scorebookdetail.entities.ScoreBookInteraction
import com.lindenlabs.scorebook.androidApp.data.model.Player

class GameViewEntityMapper {

    fun map(players: List<Player>, interaction: (ScoreBookInteraction)-> Unit) : List<ScoreBookEntity>{
        return players.map{  player ->
            ScoreBookEntity(player, { interaction(it) })
        }
    }
}
