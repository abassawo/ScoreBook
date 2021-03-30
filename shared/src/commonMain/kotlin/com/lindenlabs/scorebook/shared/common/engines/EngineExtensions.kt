package com.lindenlabs.scorebook.shared.common.engines

import kotlinx.coroutines.flow.MutableStateFlow

private fun <T> MutableStateFlow<T>.postValue(any: T) {
    this.value = any
}
