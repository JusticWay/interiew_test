package justicway.interview_cathaybk.ui.stock

import justicway.base.mvi.UiIntent

sealed class StockIntent : UiIntent {
    object OnClickNavigateBack : StockIntent()
}
