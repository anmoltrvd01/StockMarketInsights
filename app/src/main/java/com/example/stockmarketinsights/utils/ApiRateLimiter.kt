package com.example.stockmarketinsights.utils

object ApiRateLimiter {

    private const val MAX_PER_MIN = 5
    private const val MAX_PER_DAY = 25

    private var minuteCount = 0
    private var dailyCount = 0

    private var minuteWindowStart = System.currentTimeMillis()
    private var dayWindowStart = System.currentTimeMillis()

    fun canCallApi(): Boolean {
        val now = System.currentTimeMillis()

        if (now - minuteWindowStart > 60_000) {
            minuteWindowStart = now
            minuteCount = 0
        }

        if (now - dayWindowStart > 86_400_000) {
            dayWindowStart = now
            dailyCount = 0
        }

        return minuteCount < MAX_PER_MIN && dailyCount < MAX_PER_DAY
    }

    fun recordCall() {
        minuteCount++
        dailyCount++
    }
}
