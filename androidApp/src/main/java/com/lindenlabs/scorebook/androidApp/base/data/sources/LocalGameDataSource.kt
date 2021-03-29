package com.lindenlabs.scorebook.androidApp.base.data.sources

//
//class LocalGameDataSource(private val gamesDao: GameStore) : GameDataSource {
//    var games: MutableList<Game> = mutableListOf()
//
//    fun games() = games
//
//    override suspend fun load() = gamesDao.loadAll()
//
//    override suspend fun get(id: UUID): Game? = games.find { it.id == id }
//
//    override suspend fun store(game: Game) = gamesDao.insert(game)
//
//    override suspend fun update(game: Game) = gamesDao.update(game)
//
//    override suspend fun delete(game: Game) = gamesDao.delete(game)
//
//    override suspend fun clear() = gamesDao.delete(*games.toTypedArray())
//}