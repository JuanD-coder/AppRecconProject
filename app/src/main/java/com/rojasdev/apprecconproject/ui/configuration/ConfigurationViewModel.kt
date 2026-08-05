package com.rojasdev.apprecconproject.ui.configuration

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rojasdev.apprecconproject.legacy.data.dao.SettingDao
import com.rojasdev.apprecconproject.legacy.data.entities.SettingEntity
import com.rojasdev.apprecconproject.legacy.controller.dateFormat
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ConfigurationUiState(
    val priceYesAliment: SettingEntity? = null,
    val priceNoAliment: SettingEntity? = null,
    val workPrices: List<SettingEntity> = emptyList(),
    val archivedPrices: List<SettingEntity> = emptyList(),
    val canAddWorkPrice: Boolean = true,
    val isLoading: Boolean = false
)

@HiltViewModel
class ConfigurationViewModel @Inject constructor(
    private val settingDao: SettingDao
) : ViewModel() {

    private val _uiState = MutableStateFlow(ConfigurationUiState())
    val uiState: StateFlow<ConfigurationUiState> = _uiState

    init {
        loadData()
    }

    fun loadData() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            
            val yes = settingDao.getAliment("yes").firstOrNull()
            val no = settingDao.getAliment("no").firstOrNull()
            val work = settingDao.getPriceWork()
            val archived = settingDao.getAlimentArchived()
            val workCount = settingDao.getPriceWorkCount()

            _uiState.value = ConfigurationUiState(
                priceYesAliment = yes,
                priceNoAliment = no,
                workPrices = work,
                archivedPrices = archived,
                canAddWorkPrice = workCount <= 6,
                isLoading = false
            )
        }
    }

    fun updatePrice(currentSetting: SettingEntity, newCost: Int) {
        viewModelScope.launch {
            // 1. Archive current
            settingDao.updateConfig(currentSetting.Id, "archived")
            
            // 2. Insert new active
            val newSetting = SettingEntity(
                null,
                currentSetting.feeding,
                newCost,
                "active",
                dateFormat.main()
            )
            settingDao.insertConfig(newSetting)
            loadData()
        }
    }

    fun addWorkPrice(activityName: String, cost: Int) {
        viewModelScope.launch {
            val newSetting = SettingEntity(
                null,
                activityName,
                cost,
                "active",
                dateFormat.main()
            )
            settingDao.insertConfig(newSetting)
            loadData()
        }
    }
}
