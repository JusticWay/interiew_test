package justicway.interview_cathaybk.ui.stock

import justicway.base.mvi.UiIntent
import justicway.base.pojo.StockDailyInfoBO

sealed class StockIntent : UiIntent {
    data object OnClickNavigateBack : StockIntent()
    // Search
    class OnUserInputChange(val input: String) : StockIntent()
    class OnUserInputSearch(val keyword: String) : StockIntent()
    // Filter
    data object OnUserClickFilterOption : StockIntent()
    data object OnCancelFilterOption : StockIntent()
    class OnFilterChoose(val filter: FilterType) : StockIntent()
    // Detail Dialog
    class OnClickStockItem(val stock: StockDailyInfoBO) : StockIntent()
    data object OnDismissDialog : StockIntent()
}
