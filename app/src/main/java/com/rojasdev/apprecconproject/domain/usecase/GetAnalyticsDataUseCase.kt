package com.rojasdev.apprecconproject.domain.usecase

import com.rojasdev.apprecconproject.domain.model.DailyAnalyticsData
import com.rojasdev.apprecconproject.domain.repository.AnalyticsRepository
import javax.inject.Inject

class GetAnalyticsDataUseCase @Inject constructor(
    private val repository: AnalyticsRepository
) {
    suspend fun executeDailyData(date: String): DailyAnalyticsData {
        return repository.getDailyData(date)
    }

    suspend fun getDatesWithActivity(): Pair<List<String>, List<String>> {
        return Pair(
            repository.getDatesWithCollection(),
            repository.getDatesWithWork()
        )
    }
}
