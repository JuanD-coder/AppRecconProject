package com.rojasdev.apprecconproject.data.repository

import com.rojasdev.apprecconproject.legacy.data.dao.SettingDao
import com.rojasdev.apprecconproject.legacy.data.entities.SettingEntity
import javax.inject.Inject

interface SettingsRepository {
    suspend fun getActivePrice(aliment: String): SettingEntity?
    suspend fun getActiveWorkPrices(): List<SettingEntity>
}

class SettingsRepositoryImpl @Inject constructor(
    private val settingDao: SettingDao
) : SettingsRepository {
    override suspend fun getActivePrice(aliment: String): SettingEntity? {
        return settingDao.getAliment(aliment).firstOrNull()
    }

    override suspend fun getActiveWorkPrices(): List<SettingEntity> {
        return settingDao.getPriceWorkState("active")
    }
}
