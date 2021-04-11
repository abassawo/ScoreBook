package com.lindenlabs.scorebook.shared.common.viewmodels

import kotlinx.coroutines.flow.MutableStateFlow

fun <T> MutableStateFlow<T>.postValue(any: T) {
    this.value = any
}
