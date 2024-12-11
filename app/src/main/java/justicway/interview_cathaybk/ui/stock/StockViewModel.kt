package justicway.interview_cathaybk.ui.stock

import androidx.lifecycle.viewModelScope
import javax.inject.Inject
import dagger.hilt.android.lifecycle.HiltViewModel
import justicway.base.mvi.BaseViewModel
import justicway.base.repo.TaiwanStockRepository
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class StockViewModel @Inject constructor(
    private val stockRepository: TaiwanStockRepository
) :
    BaseViewModel<StockUiState, StockIntent, StockEvent>(StockUiState.initial) {

    override suspend fun handleIntent(intent: StockIntent) {
    }

    fun initial() {
        fetchStock()
    }

    private fun fetchStock() {
        viewModelScope.launch {
            val list = stockRepository.fetchStockDailyInfo()
            _uiState.update {
                it.copy(
                    isLoading = false,
                    stockList = list
                )
            }
        }
    }

}