package com.newuperapp.uper.ui.screens.home.wallet

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.newuperapp.uper.domain.model.WalletTransaction
import com.newuperapp.uper.domain.repository.DriverRepository
import com.newuperapp.uper.domain.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class WalletUiState(
    val balance: Double = 0.0,
    val transactions: List<WalletTransaction> = emptyList(),
    val isLoading: Boolean = true,
    val errorMessage: String? = null
)

@HiltViewModel
class WalletViewModel @Inject constructor(
    private val driverRepository: DriverRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(WalletUiState())
    val uiState: StateFlow<WalletUiState> = _uiState.asStateFlow()

    init {
        loadWallet()
    }

    private fun loadWallet() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
            val balanceRes = driverRepository.getWalletBalance()
            val transRes = driverRepository.getWalletTransactions()

            if (balanceRes is Resource.Success && transRes is Resource.Success) {
                _uiState.value = WalletUiState(
                    balance = balanceRes.data,
                    transactions = transRes.data,
                    isLoading = false
                )
            } else {
                val error = (balanceRes as? Resource.Error)?.message ?: (transRes as? Resource.Error)?.message
                _uiState.value = _uiState.value.copy(isLoading = false, errorMessage = error)
            }
        }
    }
}
