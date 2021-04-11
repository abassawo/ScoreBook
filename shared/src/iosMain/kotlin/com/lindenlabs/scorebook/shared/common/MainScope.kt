package com.lindenlabs.scorebook.shared.common

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext

class MainScope: CoroutineScope {
    private val dispatcher = DefaultDispatcherProvider().default()
    private val job = Job()

    override val coroutineContext: CoroutineContext
        get() = dispatcher + job
}