package justicway.interview_cathaybk.ui.stock

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.viewModels
import androidx.hilt.navigation.compose.hiltViewModel
import dagger.hilt.android.AndroidEntryPoint
import justicway.base.hasCase
import justicway.base.log
import justicway.base.mvi.BaseFragment
import justicway.base.navigateBack
import justicway.base.nullCase
import justicway.base.pojo.StockDailyInfoBO
import justicway.base.shimmerEffect
import justicway.base.toTime
import justicway.base.toUnit
import justicway.base.toast
import justicway.interview_cathaybk.ui.theme.Interview_cathaybkTheme
import justicway.interview_cathaybk.ui.theme.Pink80
import justicway.interview_cathaybk.ui.theme.Purple40
import justicway.interview_cathaybk.ui.theme.Purple80
import justicway.interview_cathaybk.ui.theme.PurpleGrey80

@AndroidEntryPoint
class StockFragment : BaseFragment<StockUiState, StockIntent, StockEvent, StockViewModel>() {
    override val viewModel: StockViewModel by viewModels()
    override fun onUiEvent(uiEvent: StockEvent) {
        when (uiEvent) {
            is StockEvent.NavigateBack -> {
                navigateBack()
            }

            is StockEvent.ShowError -> {
                 context.toast(uiEvent.message)
            }

        }
    }

    override fun onUiStateChanged(uiState: StockUiState) {
        //TODO("Not yet implemented")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        "StockFragment onCreateView".log()

        return ComposeView(requireContext()).apply {

            setContent {
                Interview_cathaybkTheme {
                    StockView()
                }
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
    Box(
        modifier = Modifier.fillMaxSize()
            .systemBarsPadding()
            .imePadding(),
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .background(Purple40),
                contentAlignment = Alignment.Center
            ) {

                Text(
                    text = "資料時間 :${System.currentTimeMillis().toTime()}",
                    color = Purple80.copy(alpha = 0.8f)
                )
            }

            Row(
                modifier = Modifier.
                fillMaxWidth()
                    .height(74.dp)
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(55.dp)
                        .background(
                            Color.Transparent,
                            shape = RoundedCornerShape(12.dp)
                        )
                        .clip(RoundedCornerShape(12.dp))
                        .border(1.dp, Color.White, shape = RoundedCornerShape(12.dp))
                ) {
                    // search input
                    TextField(
                        modifier = Modifier.fillMaxSize(),
                        value = state.input,
                        onValueChange = { newValue ->
                            // 過濾非數字輸入（可選）
                            if (newValue.all { it.isDigit() }) {
                                viewModel.sendIntent(
                                    StockIntent.OnUserInputChange(newValue)
                                )
                            }
                        },
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Number,
                            imeAction = ImeAction.Search
                        ),
                        keyboardActions = KeyboardActions(
                            onSearch = {
                                viewModel.sendIntent(
                                    StockIntent.OnUserInputSearch(state.input)
                                )
                            },
                        ),
                        label = {
                            Text("請輸入股票代號" , color = Color.DarkGray.copy(alpha = 0.8f))
                        }
                    )

                }

                Spacer(modifier = Modifier.size(12.dp))
                // search Icon
                Icon(
                    modifier = Modifier.background(Pink80, shape = RoundedCornerShape(20.dp))
                        .padding(16.dp)
                        .size(32.dp)
                        .clickable {
                            viewModel.sendIntent(
                                StockIntent.OnUserInputSearch(state.input)
                            )
                        },
                    imageVector = Icons.Default.Menu,
                    contentDescription = "filter",
                    tint = Color.Black
                )
            }
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),

                contentPadding = PaddingValues(12.dp)
            ) {
                if(state.isLoading){ // simmer
                    items(10) {
                        Box(
                            modifier = Modifier
                                .padding(6.dp)
                                .fillMaxWidth()
                                .height(200.dp)
                                .background(PurpleGrey80 , shape = RoundedCornerShape(12.dp))
                                .clip(RoundedCornerShape(12.dp))
                                .border(1.dp, Purple40, shape = RoundedCornerShape(12.dp))

                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(200.dp)
                                    .shimmerEffect(
                                        colors = listOf(
                                            Pink80.copy(alpha = 0.95f),
                                            Pink80.copy(alpha = 0.65f),
                                            Pink80.copy(alpha = 0.8f),
                                            Pink80.copy(alpha = 0.95f),
                                        )
                                    )
                            )
                        }
                    }
                }else{
                    state.filterList.hasCase {
                        items(it) { stock ->
                            Box {
                                StockInfoView(stock) {
                                    viewModel.sendIntent(StockIntent.OnClickStockItem(stock))
                                }
                            }
                        }
                    }.nullCase {
                        items(state.stockList) { stock ->
                            Box {
                                StockInfoView(stock) {
                                    viewModel.sendIntent(StockIntent.OnClickStockItem(stock))
                                }
                            }
                        }
                    }
                }
            }

        }

        if (state.isShowFilterOption) {
            FilterBottomSheet()
        }

        if (state.isShowDialog) {
            state.popDialogInfo.hasCase {
                StockDetailDialog(
                    stockInfoBO = it,
                    onDismiss = {
                        viewModel.sendIntent(StockIntent.OnDismissDialog)
                    }
                )
            }
        }
    }

}

@Preview
@Composable
fun StockInfoView(
    stockInfoBO : StockDailyInfoBO = StockDailyInfoBO(
        code = "2330",
        change = -30.00,
        name = "台積電",
        highestPrice = 1100.0,
        lowestPrice = 1000.0,
        openingPrice = 1050.0,
        closingPrice = 1080.0,
        tradeVolume = 15000,
        tradeValue = 1500000,
        transaction = 1000,
        monthlyAveragePrice = 1050.0,
        pbRatio = 20.0,
        peRatio = 30.0,
        dividendYield = 3.0,
    ),
    onClick: (StockDailyInfoBO) -> Unit = {}
) {

    Box(
        modifier = Modifier.padding(6.dp)
            .fillMaxWidth()
            .heightIn(min= 50.dp)
            .background(Pink80 , shape = RoundedCornerShape(12.dp))
            .clip(RoundedCornerShape(12.dp))
            .border(1.dp, Purple40, shape = RoundedCornerShape(12.dp))
            .padding(12.dp)
            .clickable {
                onClick(stockInfoBO)
            }
    ) {
        Column {
            Text(text = "( ${stockInfoBO.code} )" , color = Color.DarkGray )
            Text(text = stockInfoBO.name , color = Color.DarkGray , style = TextStyle.Default.copy(fontSize = 20.sp))
            Row(
                modifier = Modifier.fillMaxWidth().height(40.dp).padding(horizontal = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "開盤價" , color = Color.DarkGray ,style = TextStyle.Default.copy(fontSize = 16.sp))
                Text(
                        text = "${stockInfoBO.openingPrice}",
                        style = TextStyle.Default.copy(fontSize = 22.sp),
                        color = if(stockInfoBO.isOverMonthlyAverage(stockInfoBO.openingPrice)) Color.Red
                                else if (stockInfoBO.isLowerMonthlyAverage(stockInfoBO.openingPrice)) Color.Green
                                else Color.DarkGray
                )

                Text(text = "收盤價" , color = Color.DarkGray ,style = TextStyle.Default.copy(fontSize = 16.sp))

                Text(
                    text = "${stockInfoBO.closingPrice}",
                    style = TextStyle.Default.copy(fontSize = 22.sp),
                    color = if(stockInfoBO.isOverMonthlyAverage()) Color.Red
                    else if (stockInfoBO.isLowerMonthlyAverage()) Color.Green
                    else Color.DarkGray
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth().height(32.dp).padding(horizontal = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "最高價" , color = Color.DarkGray ,style = TextStyle.Default.copy(fontSize = 16.sp))
                Text(
                    text = "${stockInfoBO.highestPrice}" ,
                    style = TextStyle.Default.copy(fontSize = 22.sp),
                    color = if(stockInfoBO.highestPrice >stockInfoBO.openingPrice ) Color.Red
                            else if (stockInfoBO.highestPrice <stockInfoBO.openingPrice ) Color.Green
                            else Color.DarkGray
                )

                Text(text = "最低價" , color = Color.DarkGray ,style = TextStyle.Default.copy(fontSize = 16.sp))

                Text(
                    text = "${stockInfoBO.lowestPrice}",
                    style = TextStyle.Default.copy(fontSize = 22.sp),
                    color = if(stockInfoBO.lowestPrice >stockInfoBO.openingPrice ) Color.Red
                    else if (stockInfoBO.lowestPrice <stockInfoBO.openingPrice ) Color.Green
                    else Color.DarkGray
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth().height(32.dp).padding(horizontal = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "漲跌價差" , color = Color.DarkGray ,style = TextStyle.Default.copy(fontSize = 16.sp))
                Text(
                    text = "${stockInfoBO.change}",
                    style = TextStyle.Default.copy(fontSize = 22.sp),
                    color = if(stockInfoBO.change>0) Color.Red
                            else if (stockInfoBO.change<0) Color.Green
                            else Color.DarkGray
                )

                Text(text = "月平均價" , color = Color.DarkGray ,style = TextStyle.Default.copy(fontSize = 16.sp))

                Text(
                    text = "${stockInfoBO.monthlyAveragePrice}",
                    style = TextStyle.Default.copy(fontSize = 22.sp),
                    color = Color.DarkGray
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth().height(32.dp).padding(horizontal = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "成交筆數" , color = Color.DarkGray ,style = TextStyle.Default.copy(fontSize = 14.sp))
                Text(
                    text = stockInfoBO.transaction.toUnit(),
                    style = TextStyle.Default.copy(fontSize = 14.sp),
                    color = Color.DarkGray
                )

                Text(text = "成交量股數" , color = Color.DarkGray ,style = TextStyle.Default.copy(fontSize = 14.sp))
                Text(
                    text = stockInfoBO.tradeVolume.toUnit(),
                    style = TextStyle.Default.copy(fontSize = 14.sp),
                    color = Color.DarkGray
                )

                Text(text = "成交金額" , color = Color.DarkGray ,style = TextStyle.Default.copy(fontSize = 14.sp))
                Text(
                    text = stockInfoBO.tradeValue.toUnit(),
                    style = TextStyle.Default.copy(fontSize = 14.sp),
                    color = Color.DarkGray
                )
            }

        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FilterBottomSheet() {
    val viewModel: StockViewModel = hiltViewModel()
    val state by viewModel.uiState.collectAsState()
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    ModalBottomSheet(
        modifier = Modifier
            .fillMaxWidth(),
        onDismissRequest = {
            viewModel.sendIntent(StockIntent.OnCancelFilterOption)
        },
        shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
        containerColor = Purple40,
        dragHandle = {},
        contentWindowInsets = {
            WindowInsets.navigationBars
        },
        sheetState = sheetState
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.size(4.dp))
            Box(
                modifier = Modifier
                    .size(40.dp, 3.dp)
                    .background(Color.White.copy(alpha = 0.6f), CircleShape)
                    .align(Alignment.CenterHorizontally)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = "篩選條件",
                style = TextStyle.Default.copy(fontSize = 22.sp),
                color = Color.White,
                textAlign = TextAlign.Center,
            )
            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider(color = Color.White.copy(alpha = 0.1f), thickness = 1.dp)
            Column(
                modifier = Modifier.fillMaxWidth().padding(18.dp),
            ) {
                Row (
                    modifier = Modifier.clickable {
                        viewModel.sendIntent(StockIntent.OnFilterChoose(FilterType.ASC))
                    }
                ){
                    Text(
                        text = "升序",
                        style = TextStyle.Default.copy(fontSize = 22.sp),
                        color = if (state.filterType == FilterType.ASC) Color.White else Color.LightGray,
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    if (state.filterType == FilterType.ASC) {
                        Icon(
                            modifier = Modifier.size(20.dp),
                            tint = Color(0xFF8EEE80),
                            imageVector = Icons.Default.Check,
                            contentDescription = "check",
                        )
                    }
                }
                Spacer(modifier = Modifier.height(22.dp))
                Row(
                    modifier = Modifier.clickable {
                        viewModel.sendIntent(StockIntent.OnFilterChoose(FilterType.DESC))
                    }
                ) {
                    Text(
                        text = "降序",
                        style = TextStyle.Default.copy(fontSize = 22.sp),
                        color = if (state.filterType == FilterType.DESC) Color.White else Color.LightGray,
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    if (state.filterType == FilterType.DESC) {
                        Icon(
                            modifier = Modifier.size(20.dp),
                            tint = Color(0xFF8EEE80),
                            imageVector = Icons.Default.Check,
                            contentDescription = "check",
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}


@Preview
@Composable
private fun BoxScope.StockDetailDialog(
    stockInfoBO: StockDailyInfoBO,
    onDismiss: () -> Unit,
){
    Box(
        modifier = Modifier.fillMaxSize()
            .align(Alignment.Center)
            .background(Color.Black.copy(alpha = 0.5f))
            .clickable {
                onDismiss()
            }
    ){
        Box(
            modifier = Modifier.padding(6.dp)
                .fillMaxWidth().align(Alignment.Center)
                .heightIn(min= 50.dp)
                .background(Purple40 , shape = RoundedCornerShape(12.dp))
                .clip(RoundedCornerShape(12.dp))
                .border(1.dp, Purple80, shape = RoundedCornerShape(12.dp))
                .padding(12.dp)
                .clickable {
                    onDismiss()
                }
        ) {
            Column {
                Text(text = "( ${stockInfoBO.code} )" , color = Color.LightGray )
                Text(text = stockInfoBO.name , color = Color.LightGray , style = TextStyle.Default.copy(fontSize = 20.sp))
                Row(
                    modifier = Modifier.fillMaxWidth().height(32.dp).padding(horizontal = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "本益比" , color = Color.LightGray ,style = TextStyle.Default.copy(fontSize = 14.sp))
                    Text(
                        text = stockInfoBO.peRatio.toString(),
                        style = TextStyle.Default.copy(fontSize = 14.sp),
                        color = Color.LightGray
                    )

                    Text(text = "殖利率(%)" , color = Color.LightGray ,style = TextStyle.Default.copy(fontSize = 14.sp))
                    Text(
                        text = stockInfoBO.dividendYield.toString(),
                        style = TextStyle.Default.copy(fontSize = 14.sp),
                        color = Color.LightGray
                    )

                    Text(text = "股價淨值比" , color = Color.LightGray ,style = TextStyle.Default.copy(fontSize = 14.sp))
                    Text(
                        text = stockInfoBO.pbRatio.toString(),
                        style = TextStyle.Default.copy(fontSize = 14.sp),
                        color = Color.LightGray
                    )
                }

            }
        }
    }
}