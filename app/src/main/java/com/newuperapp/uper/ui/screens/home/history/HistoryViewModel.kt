package com.newuperapp.uper.ui.screens.home.history

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.newuperapp.uper.domain.model.HistoryItem
import com.newuperapp.uper.domain.repository.DriverRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.time.temporal.TemporalAdjusters
import java.util.Locale
import javax.inject.Inject

/**
 * @property days The Mon–Sun week strip shown at the top of the screen.
 * @property selectedDay The currently selected day; [trips] is filtered to this date.
 * @property trips Trips for [selectedDay] only.
 * @property totalEarned Sum of [trips] prices for [selectedDay].
 * @property totalJobs Count of [trips] for [selectedDay].
 * @property onDaySelected Callback the screen invokes when the user taps a day in the strip.
 */
data class HistoryUiState(
    val days: List<DayItem> = emptyList(),
    val selectedDay: DayItem? = null,
    val trips: List<HistoryItem> = emptyList(),
    val totalEarned: Double = 0.0,
    val totalJobs: Int = 0,
    val isLoading: Boolean = true,
    val onDaySelected: (DayItem) -> Unit = {}
)

@RequiresApi(Build.VERSION_CODES.O)
@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val driverRepository: DriverRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HistoryUiState())
    val uiState: StateFlow<HistoryUiState> = _uiState.asStateFlow()

    // Full, unfiltered trip history fetched once; the day strip filters over this.
    private var allTrips: List<HistoryItem> = emptyList()

    @RequiresApi(Build.VERSION_CODES.O)
    private val isoDateFormatter = DateTimeFormatter.ISO_LOCAL_DATE
    @RequiresApi(Build.VERSION_CODES.O)
    private val dayNameFormatter = DateTimeFormatter.ofPattern("EEE", Locale.ENGLISH)

    init {
        loadHistory()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun loadHistory() {
        _uiState.value = _uiState.value.copy(isLoading = true)
        viewModelScope.launch {
            try {
                allTrips = driverRepository.getHistory()

                val days = buildWeekStrip()
                val today = LocalDate.now().format(isoDateFormatter)
                val defaultSelected = days.firstOrNull { it.fullDate == today } ?: days.last()

                applySelection(days = days, selectedDay = defaultSelected)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(isLoading = false)
            }
        }
    }

    /**
     * Builds the Monday-to-Sunday strip for the week containing today.
     */
    @RequiresApi(Build.VERSION_CODES.O)
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

    /**
     * Called when the user taps a day in the strip. Re-filters [allTrips]
     * to that date and recomputes the totals shown in the summary cards.
     */
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