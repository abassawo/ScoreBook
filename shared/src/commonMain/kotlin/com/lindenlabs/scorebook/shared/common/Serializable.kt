//package com.lindenlabs.scorebook.shared.common
//
//import com.lindenlabs.scorebook.shared.common.raw.Player
//
//// Common Code
//@OptIn(ExperimentalMultiplatform::class)
//@OptionalExpectation
//@Target(AnnotationTarget.CLASS)
//@Retention(AnnotationRetention.BINARY)
//expect annotation class Serializable()
//
//expect class Json() {
//    fun playerToString(players: List<Player>): String
//
//    fun stringToPlayers(playerString: String): List<Player>
//}