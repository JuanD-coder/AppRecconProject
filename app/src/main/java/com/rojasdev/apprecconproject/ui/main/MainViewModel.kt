package com.rojasdev.apprecconproject.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rojasdev.apprecconproject.data.repository.SettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class MainUiState(
    val priceYesAliment: Int = 0,
    val priceNoAliment: Int = 0,
    val priceWork: Int = 0,
    val isLoading: Boolean = true
)

@HiltViewModel
class MainViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(MainUiState())
    val uiState: StateFlow<MainUiState> = _uiState

    init {
        loadPrices()
    }

    private fun loadPrices() {
        viewModelScope.launch {
            val yes = settingsRepository.getActivePrice("yes")?.cost ?: 0
            val no = settingsRepository.getActivePrice("no")?.cost ?: 0
            val work = settingsRepository.getActiveWorkPrices().firstOrNull()?.cost ?: 0
            
            _uiState.value = MainUiState(
                priceYesAliment = yes,
                priceNoAliment = no,
                priceWork = work,
                isLoading = false
            )
        }
    }
}
