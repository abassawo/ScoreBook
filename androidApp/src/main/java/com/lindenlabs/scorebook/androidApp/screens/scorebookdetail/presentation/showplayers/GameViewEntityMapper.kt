package com.lindenlabs.scorebook.androidApp.screens.scorebookdetail.presentation.showplayers

import com.lindenlabs.scorebook.androidApp.screens.scorebookdetail.entities.ScoreBookEntity
import com.lindenlabs.scorebook.androidApp.screens.scorebookdetail.entities.ScoreBookInteraction
import com.lindenlabs.scorebook.androidApp.base.data.raw.Player

class GameViewEntityMapper {

    fun map(players: List<Player>, interaction: (ScoreBookInteraction)-> Unit) : List<ScoreBookEntity>{
        return players.map{  player ->
            ScoreBookEntity(player, { interaction(it) })
        }
    }
}
