package com.lindenlabs.scorebook.androidApp.data

import com.lindenlabs.scorebook.androidApp.data.persistence.GamesDatabase
import com.lindenlabs.scorebook.androidApp.screens.home.data.model.Game
import com.lindenlabs.scorebook.androidApp.screens.home.data.model.Player
import java.util.*

class PersistentGameRepository(
    val gameRepository: GameRepository,
    val gamesDatabase: GamesDatabase
) : GameDataSource {

    override val games: List<Game> = gameRepository.games

    override fun getGameById(id: UUID): Game? = gameRepository.getGameById(id)

    override fun storeGame(game: Game) {
        gameRepository.storeGame(game)
        gamesDatabase.games().insert(game)
    }

    override fun updateGame(game: Game, lastPlayer: Player, newScore: Int) {
        gameRepository.updateGame(game, lastPlayer, newScore)
        gamesDatabase.games().update(game)
    }

    override fun updatePlayers(game: Game, player: List<Player>): List<Player> {
        gameRepository.updatePlayers(game, player)
        gamesDatabase.games().update(game)
        return game.players
    }

    override fun addPlayer(game: Game, player: Player): List<Player> {
        TODO("Not yet implemented")
    }

}