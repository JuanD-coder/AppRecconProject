package com.rojasdev.apprecconproject.ui.recollection

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rojasdev.apprecconproject.data.repository.CollectorRepository
import com.rojasdev.apprecconproject.data.repository.RecollectionRepository
import com.rojasdev.apprecconproject.domain.usecase.GetCollectorsUseCase
import com.rojasdev.apprecconproject.domain.usecase.GetCollectorsWithTotalUseCase
import com.rojasdev.apprecconproject.legacy.data.dao.RecolectoresDao
import com.rojasdev.apprecconproject.legacy.data.dataModel.collecionTotalCollector
import com.rojasdev.apprecconproject.legacy.data.entities.RecolectoresEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import com.rojasdev.apprecconproject.legacy.data.dao.SettingDao
import javax.inject.Inject

data class RecollectionUiState(
    val collectors: List<RecolectoresEntity> = emptyList(),
    val collectionTotals: List<collecionTotalCollector> = emptyList(),
    val totalKg: Double = 0.0,
    val totalAmount: Double = 0.0,
    val isLoading: Boolean = false
)

@HiltViewModel
class RecollectionViewModel @Inject constructor(
    private val getCollectorsUseCase: GetCollectorsUseCase,
    private val getCollectorsWithTotalUseCase: GetCollectorsWithTotalUseCase,
    private val recollectionRepository: RecollectionRepository,
    private val recolectoresDao: RecolectoresDao,
    private val settingDao: SettingDao
) : ViewModel() {

    private val _uiState = MutableStateFlow(RecollectionUiState())
    val uiState: StateFlow<RecollectionUiState> = _uiState

    init {
        loadData()
    }

    fun loadData() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            val collectors = getCollectorsUseCase()
            val totals = getCollectorsWithTotalUseCase()
            val summary = settingDao.getTotalCollectionActive()
            
            val totalKg = if (summary.isNotEmpty()) summary[0].cantidad else 0.0
            val totalAmount = if (summary.isNotEmpty()) summary[0].total else 0.0

            _uiState.value = RecollectionUiState(
                collectors = collectors,
                collectionTotals = totals,
                totalKg = totalKg,
                totalAmount = totalAmount,
                isLoading = false
            )
        }
    }

    fun archiveCollector(id: Int) {
        viewModelScope.launch {
            recolectoresDao.updateCollectorState(id)
            recollectionRepository.archiveCollection(id)
            loadData()
        }
    }

    fun addCollector(name: String) {
        viewModelScope.launch {
            val entity = RecolectoresEntity(null, name, "active")
            recolectoresDao.add(entity)
            loadData()
        }
    }
}
