package justicway.base.mvi


import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import justicway.base.log
import kotlinx.coroutines.launch


abstract class BaseFragment<uiState : UiState, uiIntent : UiIntent, uiEvent : UiEvent, ViewModel : BaseViewModel<uiState, uiIntent, uiEvent>> :
    Fragment() {

    protected abstract val viewModel: ViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        "${this.javaClass.name} onViewCreated".log()
        observeUiState()
        observeUiEvent()

    }

    private fun observeUiState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState
                .collect { newUiState ->
                    onUiStateChanged(newUiState)
                    viewModel.setUiState(newUiState)
                }
        }
    }

    private fun observeUiEvent() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiEvent
                .collect {
                    onUiEvent(it)
                }
        }

    }

    protected abstract fun onUiStateChanged(uiState: uiState)

    protected abstract fun onUiEvent(uiEvent: uiEvent)
}