package com.lindenlabs.scorebook.androidApp.di

import com.lindenlabs.scorebook.androidApp.di.scope.FragmentScope
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module
class HomeModule {
    @Provides
    @FragmentScope
    fun provideViewModelFactory(gameId: String): ViewModelFactory =
        ViewModelFactory(gameId)
}

@Module
class AddPlayersArgsModule(private val gameId: String) {

    @Provides
    fun provideArg(): String = gameId

    @Provides
    @FragmentScope
    fun provideViewModelFactory(gameId: String): ViewModelFactory =
        ViewModelFactory(gameId)
}


@Module
class UpdatePointsModule(val gameId: String, val playerId: String) {

    @Provides
    @FragmentScope
    @Named("gameId")
    fun provideGameId(): String = gameId

    @Provides
    @FragmentScope
    @Named("playerId")
    fun providePlayerId(): String = playerId

    @Provides
    @FragmentScope
    fun provideViewModelFactory(@Named("gameId") gameId: String, @Named("playerId") playerId: String): ViewModelFactory =
        ViewModelFactory(gameId, playerId)
}


@Module
class GameScoreModule(val gameId: String) {

    @Provides
    @FragmentScope
    fun provideArg(): String = gameId

    @Provides
    @FragmentScope
    fun provideViewModelFactory(gameId: String): ViewModelFactory =
        ViewModelFactory(gameId)
}

@Module
class EditGameModule(val gameId: String) {

    @Provides
    @FragmentScope
    fun provideArg(): String = gameId

    @Provides
    @FragmentScope
    fun provideViewModelFactory(gameId: String): ViewModelFactory =
        ViewModelFactory(gameId)

}

@Module
class VictoryModule(private val gameId: String) {

    @Provides
    @FragmentScope
    fun provideArg(): String = gameId

    @Provides
    @FragmentScope
    fun provideViewModelFactory(gameId: String): ViewModelFactory =
        ViewModelFactory(gameId)
}




