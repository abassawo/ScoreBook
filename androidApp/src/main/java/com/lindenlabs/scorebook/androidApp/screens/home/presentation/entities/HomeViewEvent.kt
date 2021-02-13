package com.lindenlabs.scorebook.androidApp.screens.home.presentation.entities

sealed class HomeViewEvent {
    object AlertNoTextEntered : HomeViewEvent()
}