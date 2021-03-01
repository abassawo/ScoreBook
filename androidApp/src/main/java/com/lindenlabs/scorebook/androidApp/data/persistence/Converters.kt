package com.lindenlabs.scorebook.androidApp.data.persistence

import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.google.gson.Gson
import com.lindenlabs.scorebook.androidApp.screens.home.data.model.GameOutcome
import com.lindenlabs.scorebook.androidApp.screens.home.data.model.Player
import com.lindenlabs.scorebook.androidApp.screens.home.presentation.GameStrategy
import org.json.JSONArray
import org.json.JSONObject
import timber.log.Timber
import java.util.*

fun JSONObject.toPlayer(): Player {
    Timber.d("Converter player $this")
    return Player(name = this.optString("name"))
}

private fun JSONArray.toPlayer(): List<Player> {
    val players = mutableListOf<Player>()
    for (i in 0 until length()) {
        val jsonObj = this.getJSONObject(i)
        players += jsonObj.toPlayer()
    }
    return players
}

interface Converters {

    @TypeConverters
    class StrategyConverter {

        @TypeConverter
        fun strategyToString(strategy: GameStrategy) = strategy.toString()

        @TypeConverter
        fun stringToStrategy(strategy: String) = GameStrategy.valueOf(strategy)
    }

    @TypeConverters
    class UUIDConverter {

        @TypeConverter
        fun uuidToString(uuid: UUID) = uuid.toString()

        @TypeConverter
        fun stringToUUID(uuid: String) = UUID.fromString(uuid)
    }

    @TypeConverters
    class PlayerConverter {

        @TypeConverter
        fun playerToString(player: Player): String = Gson().toJson(player)

        @TypeConverter
        fun stringToPlayer(playerString: String): Player {
            val jsonObject = JSONObject(playerString)
            return jsonObject.toPlayer()
        }
    }


    @TypeConverters
    class PlayerListConverter {

        @TypeConverter
        fun playerToString(players: List<Player>): String = buildString {
            players.forEach { player ->
                append(Gson().toJson(player))
            }
        }

        @TypeConverter
        fun stringToPlayer(playerString: String): List<Player> {
            return JSONArray(playerString).toPlayer()
        }
    }

    @TypeConverters
    class OutcomeConverter {

        @TypeConverter
        fun outcomeToString(outcome: GameOutcome) = Gson().toJson(outcome)

        @TypeConverter
        fun stringToOutcome(outcome: String) = Gson().fromJson(outcome, GameOutcome::class.java)
    }
}