package com.lindenlabs.scorebook.shared.common.data

actual class Platform actual constructor() {
    actual val platform: String = "Android ${android.os.Build.VERSION.SDK_INT}"
}