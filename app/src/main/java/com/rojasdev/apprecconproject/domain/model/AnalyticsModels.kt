package com.rojasdev.apprecconproject.domain.model

import com.rojasdev.apprecconproject.legacy.data.dataModel.allCollecionAndCollector
import com.rojasdev.apprecconproject.legacy.data.dataModel.allWorkAndCollector

data class DailyAnalyticsData(
    val collectionRecords: List<allCollecionAndCollector>,
    val workRecords: List<allWorkAndCollector>,
    val totalKg: Double,
    val totalCollectionMoney: Double,
    val totalWorkDays: Double,
    val totalWorkMoney: Double
)
