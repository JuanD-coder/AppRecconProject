package com.rojasdev.apprecconproject.ui.labor

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rojasdev.apprecconproject.legacy.data.dao.RecolectoresDao
import com.rojasdev.apprecconproject.legacy.data.dao.SettingDao
import com.rojasdev.apprecconproject.legacy.data.dataModel.workTotalCollector
import com.rojasdev.apprecconproject.legacy.data.entities.RecolectoresEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class LaborUiState(
    val workers: List<RecolectoresEntity> = emptyList(),
    val laborTotals: List<workTotalCollector> = emptyList(),
    val totalDays: Double = 0.0,
    val totalAmount: Double = 0.0,
    val isLoading: Boolean = false
)

@HiltViewModel
class LaborViewModel @Inject constructor(
    private val recolectoresDao: RecolectoresDao,
    private val settingDao: SettingDao
) : ViewModel() {

    private val _uiState = MutableStateFlow(LaborUiState())
    val uiState: StateFlow<LaborUiState> = _uiState

    init {
        loadData()
    }

    fun loadData() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            val workers = recolectoresDao.getAllWorkMen()
            
            val totals = mutableListOf<workTotalCollector>()
            val ids = recolectoresDao.getIDManWork()
            for (id in ids) {
                val t = recolectoresDao.getManAndWorkTotal(id.toInt())
                if (t.isNotEmpty() && t[0].name_recolector != null) {
                    totals.add(t[0])
                }
            }

            val summary = settingDao.getTotalWorkActive()
            val days = if (summary.isNotEmpty()) summary[0].cantidad else 0.0
            val amount = if (summary.isNotEmpty()) summary[0].total else 0.0

            _uiState.value = LaborUiState(
                workers = workers,
                laborTotals = totals,
                totalDays = days,
                totalAmount = amount,
                isLoading = false
            )
        }
    }
}
