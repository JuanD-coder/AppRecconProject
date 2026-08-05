package com.rojasdev.apprecconproject.domain.repository

import com.rojasdev.apprecconproject.domain.model.DailyAnalyticsData

interface AnalyticsRepository {
    suspend fun getDailyData(date: String): DailyAnalyticsData
    suspend fun getDatesWithCollection(): List<String>
    suspend fun getDatesWithWork(): List<String>
}
