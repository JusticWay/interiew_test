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
        when (intent) {
            is StockIntent.OnClickNavigateBack -> {
                emitUiEvent(StockEvent.NavigateBack)
            }
            is StockIntent.OnUserInputChange -> {
                _uiState.update {
                    it.copy(
                        input = intent.input,
                        filterList = it.stockList.filter {
                            it.code.contains(intent.input) || it.name.contains(intent.input)
                        }
                    )
                }
            }
            is StockIntent.OnUserInputSearch -> {
                _uiState.update {
                    it.copy(
                        isShowFilterOption = true,
                        filterList = it.stockList.filter {
                            it.code.contains(intent.keyword) || it.name.contains(intent.keyword)
                        }
                    )
                }
            }

            is StockIntent.OnUserClickFilterOption -> {
                _uiState.update {
                    it.copy(
                        filterList = it.stockList.sortedBy {
                            it.code
                        }
                    )
                }
            }

            is StockIntent.OnCancelFilterOption -> {
                _uiState.update {
                    it.copy(
                        filterList = null
                    )
                }
            }

            is StockIntent.OnFilterChoose -> {
                _uiState.update {
                    it.copy(
                        isShowFilterOption = false,
                        filterType = intent.filter,
                        stockList = when (intent.filter) {
                            FilterType.ASC -> it.stockList.sortedBy { it2->
                                it2.code
                            }
                            FilterType.DESC -> it.stockList.sortedByDescending { it2->
                                it2.code
                            }
                        },
                        filterList = when (intent.filter) {
                            FilterType.ASC -> it.filterList?.filter { it2->
                                it2.code.contains(uiState.value.input)
                            }?.sortedBy { it3->
                                it3.code
                            }
                            FilterType.DESC -> it.filterList?.filter { it2->
                                it2.code.contains(uiState.value.input)
                            }?.sortedByDescending { it3->
                                it3.code
                            }
                        }
                    )
                }
            }

            is StockIntent.OnClickStockItem -> {
                _uiState.update {
                    it.copy(
                        isShowDialog = true,
                        popDialogInfo = intent.stock
                    )
                }
            }
            is StockIntent.OnDismissDialog -> {
                _uiState.update {
                    it.copy(
                        isShowDialog = false,
                        popDialogInfo = null
                    )
                }
            }

        }
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