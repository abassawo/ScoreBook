package com.lindenlabs.scorebook.shared.common.viewmodels

abstract class AbstractEngine<T : BaseInteraction> {

    abstract fun handleInteraction(interaction: T)
}