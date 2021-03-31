package com.lindenlabs.scorebook.shared.common

expect class DriverFactory {
    expect fun createDriver(): SqlDriver
}
