package com.lindenlabs.scorebook.androidApp.data.persistence

import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import com.lindenlabs.scorebook.androidApp.screens.home.data.model.GameOutcome
import com.lindenlabs.scorebook.androidApp.screens.home.data.model.Player
import com.lindenlabs.scorebook.androidApp.screens.home.presentation.GameStrategy
import java.util.*
import kotlin.collections.ArrayList


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
            val myType = object : TypeToken<List<JsonObject>>() {}.type
            return Gson().fromJson(playerString, Player::class.java)
        }
    }

    @TypeConverters
    class PlayerListConverter {

        @TypeConverter
        fun playerToString(player: List<Player>): String = Gson().toJson(player)

        @TypeConverter
        fun stringToPlayer(playerString: String): List<Player> {
            val myType = object : TypeToken<List<JsonObject>>() {}.type
            return Gson().fromJson<List<JsonObject>>(playerString, myType) as ArrayList<Player>
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