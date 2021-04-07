package com.lindenlabs.scorebook.shared.common.engines

abstract class AbstractEngine<T : BaseInteraction> {

    abstract fun handleInteraction(interaction: T)
}