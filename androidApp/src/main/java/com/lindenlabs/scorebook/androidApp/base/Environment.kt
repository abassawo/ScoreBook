package com.lindenlabs.scorebook.androidApp.base

import com.lindenlabs.scorebook.androidApp.base.domain.GameDataSource


class Environment(private val gamesRepo: GameDataSource) : GameDataSource by gamesRepo