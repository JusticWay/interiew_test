package justicway.interview_cathaybk.ui.stock

import justicway.base.mvi.UiEvent

/**
 * 定義一個 觸發 Controller 端的事件
 */
sealed class StockEvent : UiEvent {
    data object NavigateBack : StockEvent()
    data object RequirePermission : StockEvent()
    class ShowError(val message: String) : StockEvent()
}