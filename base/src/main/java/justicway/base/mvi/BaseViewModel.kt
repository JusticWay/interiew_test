package justicway.base.mvi


import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import justicway.base.default
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

abstract class BaseViewModel<uiState : UiState, uiIntent : UiIntent, uiEvent : UiEvent>(
    initialState: uiState,
    private val savedStateHandle: SavedStateHandle? = null
) : ViewModel() {

    protected val _uiState = MutableStateFlow(savedStateHandle?.get<uiState>("UiState").default(initialState))
    val uiState: StateFlow<uiState> = _uiState.asStateFlow()

    protected val _uiEvent = MutableSharedFlow<uiEvent>()
    val uiEvent: SharedFlow<uiEvent> = _uiEvent.asSharedFlow()

    private val _uiIntent: Channel<uiIntent> = Channel()
    private val uiIntent: Flow<uiIntent> = _uiIntent.receiveAsFlow()

    init {
        viewModelScope.launch {
            uiIntent.collect {
                handleIntent(it)
            }
        }
    }

    abstract suspend fun handleIntent(intent: uiIntent)

    fun sendIntent(intent: uiIntent) {
        viewModelScope.launch {
            _uiIntent.send(intent)
        }
    }

    fun setUiState(uiState: uiState) {
        savedStateHandle?.set("UiState", uiState)
    }

    protected suspend fun emitUiEvent(event: uiEvent) {
        _uiEvent.emit(event)
    }


}