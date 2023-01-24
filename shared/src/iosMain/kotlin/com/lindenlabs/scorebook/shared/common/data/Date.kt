//package com.lindenlabs.scorebook.shared.common.data
//
//import platform.Foundation.*
//import kotlin.time.ExperimentalTime
//import kotlin.time.milliseconds
//import kotlin.time.seconds
//
//actual class Date {
//
//    private var date: NSDate
//
//    actual constructor() {
//        date = NSDate()
//    }
//
//    @OptIn(ExperimentalTime::class)
//    actual constructor(time: Long) {
//        date = NSDate.dateWithTimeIntervalSince1970(time.milliseconds.inSeconds)
//    }
//
//    constructor(platformDate: NSDate) {
//        date = platformDate
//    }
//
//    actual fun compareTo(other: Date): Int {
//        return when (this.date.compare(other.date)) {
//            NSOrderedAscending -> -1
//            NSOrderedDescending -> 1
//            else -> 0
//        }
//    }
//
//    @OptIn(ExperimentalTime::class)
//    actual fun getTime(): Long = date.timeIntervalSince1970.seconds.inMilliseconds.toLong()
//
//
//    fun toPlatformDate() = date
//}