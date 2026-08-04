package com.rojasdev.apprecconproject.ui.analytics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rojasdev.apprecconproject.legacy.data.dao.RecolectoresDao
import com.rojasdev.apprecconproject.legacy.data.dao.RecollectionDao
import com.rojasdev.apprecconproject.legacy.data.dao.WorkDao
import com.rojasdev.apprecconproject.legacy.data.dataModel.allCollecionAndCollector
import com.rojasdev.apprecconproject.legacy.data.dataModel.allWorkAndCollector
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

data class CalendarDay(
    val date: String,
    val dayOfMonth: String,
    val isSelected: Boolean = false,
    val hasCollection: Boolean = false,
    val hasWork: Boolean = false,
    val isCurrentMonth: Boolean = true
)

data class AnalyticsUiState(
    val selectedDate: String = "",
    val currentMonth: Int = 0, // 1-12
    val currentYear: Int = 0,
    val monthName: String = "",
    val calendarDays: List<CalendarDay> = emptyList(),
    val dailyCollectionRecords: List<allCollecionAndCollector> = emptyList(),
    val dailyWorkRecords: List<allWorkAndCollector> = emptyList(),
    val totalKg: Double = 0.0,
    val totalCollectionMoney: Double = 0.0,
    val totalWorkDays: Double = 0.0,
    val totalWorkMoney: Double = 0.0,
    val isLoading: Boolean = false
)

@HiltViewModel
class AnalyticsViewModel @Inject constructor(
    private val recolectoresDao: RecolectoresDao,
    private val recollectionDao: RecollectionDao,
    private val workDao: WorkDao
) : ViewModel() {

    private val _uiState = MutableStateFlow(AnalyticsUiState())
    val uiState: StateFlow<AnalyticsUiState> = _uiState

    private val calendar = Calendar.getInstance()

    init {
        val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        _uiState.value = _uiState.value.copy(
            selectedDate = today,
            currentMonth = calendar.get(Calendar.MONTH) + 1,
            currentYear = calendar.get(Calendar.YEAR)
        )
        updateCalendar()
        loadDayData(today)
    }

    fun onDateSelected(date: String) {
        _uiState.value = _uiState.value.copy(selectedDate = date)
        updateCalendar()
        loadDayData(date)
    }

    fun nextMonth() {
        calendar.add(Calendar.MONTH, 1)
        updateCalendarState()
    }

    fun previousMonth() {
        calendar.add(Calendar.MONTH, -1)
        updateCalendarState()
    }

    private fun updateCalendarState() {
        _uiState.value = _uiState.value.copy(
            currentMonth = calendar.get(Calendar.MONTH) + 1,
            currentYear = calendar.get(Calendar.YEAR)
        )
        updateCalendar()
    }

    private fun updateCalendar() {
        viewModelScope.launch {
            val month = _uiState.value.currentMonth
            val year = _uiState.value.currentYear
            
            val sdf = SimpleDateFormat("MMMM", Locale("es", "ES"))
            val cal = Calendar.getInstance()
            cal.set(year, month - 1, 1)
            val monthName = sdf.format(cal.time).uppercase()

            // Fetch days with data from DB
            val collectionDates = recollectionDao.getDateCollection().map { it.substring(0, 10) }
            val workDates = workDao.getDateWork().map { it.substring(0, 10) }

            val days = mutableListOf<CalendarDay>()
            
            // First day of month
            cal.set(Calendar.DAY_OF_MONTH, 1)
            val firstDayOfWeek = cal.get(Calendar.DAY_OF_WEEK) // Sunday = 1
            
            // Days from previous month to fill start of week (assuming Monday start like legacy)
            // But legacy seems to use domingo as end of week list in getDaysMonth. 
            // Let's use a simpler Grid approach 7 columns.
            
            val daysInMonth = cal.getActualMaximum(Calendar.DAY_OF_MONTH)
            
            for (i in 1..daysInMonth) {
                val dateStr = String.format("%04d-%02d-%02d", year, month, i)
                days.add(CalendarDay(
                    date = dateStr,
                    dayOfMonth = i.toString(),
                    isSelected = dateStr == _uiState.value.selectedDate,
                    hasCollection = collectionDates.any { it == dateStr },
                    hasWork = workDates.any { it == dateStr }
                ))
            }
            
            _uiState.value = _uiState.value.copy(
                monthName = monthName,
                calendarDays = days
            )
        }
    }

    private fun loadDayData(date: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            
            val collectionRecords = mutableListOf<allCollecionAndCollector>()
            val workRecords = mutableListOf<allWorkAndCollector>()
            
            val allIds = recolectoresDao.getAll()
            for (id in allIds) {
                val coll = recolectoresDao.getAllCollectorAndCollectionId("$date%", id.toInt())
                if (coll.isNotEmpty() && coll[0].name_recolector != null) {
                    collectionRecords.add(coll[0])
                }
                
                val work = recolectoresDao.getAllCollectorAndWorkId("$date%", id.toInt())
                if (work.isNotEmpty() && work[0].name_recolector != null) {
                    workRecords.add(work[0])
                }
            }

            val totalColl = recollectionDao.getTotalKgDate("$date%")
            val totalWor = workDao.getTotalWorkDate("$date%")

            _uiState.value = _uiState.value.copy(
                dailyCollectionRecords = collectionRecords,
                dailyWorkRecords = workRecords,
                totalKg = totalColl.Cantidad,
                totalCollectionMoney = totalColl.result,
                totalWorkDays = totalWor.Cantidad,
                totalWorkMoney = totalWor.result,
                isLoading = false
            )
        }
    }
}
