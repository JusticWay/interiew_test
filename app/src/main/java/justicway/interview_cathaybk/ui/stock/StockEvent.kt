package justicway.interview_cathaybk.ui.stock

import justicway.base.mvi.UiEvent

sealed class StockEvent : UiEvent {
    data object NavigateBack : StockEvent()
}