package com.lindenlabs.scorebook.androidApp.data.persistence

import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.lindenlabs.scorebook.androidApp.screens.home.data.model.GameOutcome
import com.lindenlabs.scorebook.androidApp.screens.home.data.model.Player
import com.lindenlabs.scorebook.androidApp.screens.home.presentation.GameStrategy
import org.json.JSONArray
import org.json.JSONObject
import timber.log.Timber
import java.lang.reflect.Type
import java.util.*


fun JSONObject.toPlayer(): Player {
    Timber.d("Converter player $this")
    return Gson().fromJson(this.toString(), Player::class.java)
}

internal inline fun <reified T> Gson.fromJsonAsList(json: String) =
    fromJson<T>(json, object : TypeToken<T>() {}.type)


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
        fun playerToString(players: List<Player>): String = Gson().toJson(players.toTypedArray())

        @TypeConverter
        fun stringToPlayer(playerString: String): List<Player> {
            Timber.e("Player string $playerString")
            val playerListType: Type = object : TypeToken<ArrayList<Player?>?>() {}.type
            return Gson().fromJson(playerString, playerListType)
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