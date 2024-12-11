package justicway.interview_cathaybk.ui.stock

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.fragment.app.viewModels
import androidx.hilt.navigation.compose.hiltViewModel
import dagger.hilt.android.AndroidEntryPoint
import justicway.base.hasCase
import justicway.base.mvi.BaseFragment
import justicway.base.navigateBack
import justicway.base.nullCase

@AndroidEntryPoint
class StockFragment : BaseFragment<StockUiState, StockIntent, StockEvent, StockViewModel>() {
    override val viewModel: StockViewModel by viewModels()
    override fun onUiEvent(uiEvent: StockEvent) {
        when (uiEvent) {
            is StockEvent.NavigateBack -> {
                navigateBack()
            }
        }
    }

    override fun onUiStateChanged(uiState: StockUiState) {
        //TODO("Not yet implemented")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                StockView()
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        parseIntent()
    }

    private fun parseIntent() {
        arguments?.hasCase {
            viewModel.initial()
        }.nullCase {
            viewModel.initial()
        }
    }


}

@Composable
fun StockView() {
    val viewModel: StockViewModel = hiltViewModel()
    val state by viewModel.uiState.collectAsState()
    Column(
        modifier = Modifier.fillMaxSize(),
    ) {
        Text(text = "StockView" , color = Color.Yellow)
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            contentPadding = PaddingValues(12.dp)
        ) {

            items(state.stockList) { stock ->
                Box {
                    Text(text = stock.name , color = Color.Blue)
                }
            }

        }

    }
}

@Preview
@Composable
fun PreviewStockView() {
    StockView()
}
