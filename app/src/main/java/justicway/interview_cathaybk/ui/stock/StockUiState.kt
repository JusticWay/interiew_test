package justicway.interview_cathaybk.ui.stock

import android.app.Dialog
import justicway.base.mvi.UiState
import justicway.base.pojo.StockDailyInfoBO

data class StockUiState(
    val isLoading: Boolean,
    val stockList: List<StockDailyInfoBO> = emptyList(),
    val filterList: List<StockDailyInfoBO>? = null,
    val filterType: FilterType = FilterType.ASC,
    val input: String = "",
    val isShowFilterOption: Boolean = false,
    val isShowDialog: Boolean = false,
    val popDialogInfo: StockDailyInfoBO? = null
) : UiState {
    companion object {
        val initial = StockUiState(
            isLoading = true
        )
    }
}