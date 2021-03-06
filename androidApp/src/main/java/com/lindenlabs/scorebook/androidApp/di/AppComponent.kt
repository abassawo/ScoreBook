package com.lindenlabs.scorebook.androidApp.di

import com.lindenlabs.scorebook.androidApp.MainActivity
import com.lindenlabs.scorebook.androidApp.ScoreBookApplication
import dagger.Component
import javax.inject.Singleton

@Component(modules = [AppModule::class])
@Singleton
interface  AppComponent {
    fun inject(mainActivity: MainActivity)
    fun inject(app: ScoreBookApplication)
}
