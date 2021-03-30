package com.lindenlabs.scorebook.shared.common.data

interface DataSource<T> {

    var items: MutableList<T>

    suspend fun load(): List<T>

    suspend fun get(id: String) : T

    suspend fun store(t: T)

    suspend fun update(t: T)

    suspend fun delete(t: T)

    suspend fun clear()
}