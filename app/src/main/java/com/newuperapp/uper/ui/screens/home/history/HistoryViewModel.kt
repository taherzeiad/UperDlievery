package com.newuperapp.uper.ui.screens.home.history

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.newuperapp.uper.domain.model.HistoryItem
import com.newuperapp.uper.domain.repository.DriverRepository
import com.newuperapp.uper.domain.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalAdjusters
import java.util.Locale
import javax.inject.Inject

data class HistoryUiState(
    val days: List<DayItem> = emptyList(),
    val selectedDay: DayItem? = null,
    val trips: List<HistoryItem> = emptyList(),
    val totalEarned: Double = 0.0,
    val totalJobs: Int = 0,
    val isLoading: Boolean = true,
    val errorMessage: String? = null,
    val onDaySelected: (DayItem) -> Unit = {}
)

@RequiresApi(Build.VERSION_CODES.O)
@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val driverRepository: DriverRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HistoryUiState())
    val uiState: StateFlow<HistoryUiState> = _uiState.asStateFlow()

    private var allTrips: List<HistoryItem> = emptyList()

    private val isoDateFormatter = DateTimeFormatter.ISO_LOCAL_DATE
    private val dayNameFormatter = DateTimeFormatter.ofPattern("EEE", Locale.ENGLISH)

    init {
        loadHistory()
    }

    private fun loadHistory() {
        _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
        viewModelScope.launch {
            when (val result = driverRepository.getHistory()) {
                is Resource.Success -> {
                    allTrips = result.data
                    val days = buildWeekStrip()
                    val today = LocalDate.now().format(isoDateFormatter)
                    val defaultSelected = days.firstOrNull { it.fullDate == today } ?: days.last()
                    applySelection(days = days, selectedDay = defaultSelected)
                }
                is Resource.Error -> {
                    _uiState.value = _uiState.value.copy(isLoading = false, errorMessage = result.message)
                }
                else -> {}
            }
        }
    }

    private fun buildWeekStrip(): List<DayItem> {
        val today = LocalDate.now()
        val startOfWeek = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
        return (0..6).map { offset ->
            val date = startOfWeek.plusDays(offset.toLong())
            DayItem(
                dayName = date.format(dayNameFormatter),
                dayNumber = date.dayOfMonth.toString(),
                fullDate = date.format(isoDateFormatter)
            )
        }
    }

    private fun onDaySelected(day: DayItem) {
        applySelection(days = _uiState.value.days, selectedDay = day)
    }

    private fun applySelection(days: List<DayItem>, selectedDay: DayItem) {
        val filtered = allTrips.filter { it.date == selectedDay.fullDate }
        _uiState.value = _uiState.value.copy(
            days = days,
            selectedDay = selectedDay,
            trips = filtered,
            totalEarned = filtered.sumOf { it.price },
            totalJobs = filtered.size,
            isLoading = false,
            onDaySelected = ::onDaySelected
        )
    }
}
