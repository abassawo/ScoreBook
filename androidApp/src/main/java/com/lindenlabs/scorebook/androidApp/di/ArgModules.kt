package com.lindenlabs.scorebook.androidApp.di

import com.lindenlabs.scorebook.androidApp.di.scope.FragmentScope
import com.lindenlabs.scorebook.shared.common.data.AppRepository
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module
class HomeModule {
    @Provides
    @FragmentScope
    fun provideViewModelFactory(gameId: String, appRepository: AppRepository): ViewModelFactory =
        ViewModelFactory(appRepository, gameId)
}

@Module
class AddPlayersArgsModule(private val gameId: String, val appRepository: AppRepository) {

    @Provides
    fun provideArg(): String = gameId

    @Provides
    @FragmentScope
    fun provideViewModelFactory(gameId: String): ViewModelFactory =
        ViewModelFactory(appRepository, gameId)
}


@Module
class UpdatePointsModule(val appRepository: AppRepository, val gameId: String, val playerId: String) {

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
    fun provideViewModelFactory(
        @Named("gameId") gameId: String,
        @Named("playerId") playerId: String
    ): ViewModelFactory =
        ViewModelFactory(appRepository = appRepository , gameId, playerId)
}


@Module
class GameScoreModule(val gameId: String, val appRepository: AppRepository) {

    @Provides
    @FragmentScope
    fun provideArg(): String = gameId

    @Provides
    @FragmentScope
    fun provideViewModelFactory(gameId: String): ViewModelFactory =
        ViewModelFactory(appRepository, gameId)
}

@Module
class EditGameModule( val appRepository: AppRepository, val gameId: String) {

    @Provides
    @FragmentScope
    fun provideArg(): String = gameId

    @Provides
    @FragmentScope
    fun provideViewModelFactory(gameId: String): ViewModelFactory =
        ViewModelFactory(appRepository, gameId)

}

@Module
class VictoryModule( val appRepository: AppRepository, private val gameId: String) {

    @Provides
    @FragmentScope
    fun provideArg(): String = gameId

    @Provides
    @FragmentScope
    fun provideViewModelFactory(gameId: String): ViewModelFactory =
        ViewModelFactory(appRepository, gameId)
}




