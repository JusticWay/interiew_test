package justicway.interview_cathaybk.ui.stock

import justicway.base.mvi.UiState
import justicway.base.pojo.StockDailyInfoBO

data class StockUiState(
    val isLoading: Boolean,
    val stockList: List<StockDailyInfoBO> = emptyList()
) : UiState {
    companion object {
        val initial = StockUiState(
            isLoading = true
        )
    }
}