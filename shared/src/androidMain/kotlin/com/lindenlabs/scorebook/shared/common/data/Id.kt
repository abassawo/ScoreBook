package com.lindenlabs.scorebook.shared.common.data

import java.util.*

actual class Id actual constructor() {
    actual val id: String = UUID.randomUUID().toString()
}
