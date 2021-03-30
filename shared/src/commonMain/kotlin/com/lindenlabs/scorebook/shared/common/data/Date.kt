package com.lindenlabs.scorebook.shared.common.data

expect class Date {

    /**
     * Get a date at the current instant.
     */
    constructor()

    /**
     * Get a date at the specified instant.
     *
     * @param time: The amount of milliseconds since `1970-01-01T00:00:00Z`.
     */
    constructor(time: Long)

    /**
     * Compare this instance with the other instance.
     *
     * @param other: Another date instance.
     * @return 0 if the other is equal to this instance; a value less than 0 if this instance is before the other;
     *          and a value greater than 0 if this instance is after the other.
     */
    fun compareTo(other: Date): Int

    /**
     * Get the amount of milliseconds since `1970-01-01T00:00:00Z`.
     *
     * @return The amount of milliseconds since reference date.
     */
    fun getTime(): Long

}