package justicway.base.retrofit.response.twse

import com.google.gson.annotations.SerializedName

data class FetchStockDailyAvgResponse(
    @SerializedName("ClosingPrice")
    val closingPrice: String,
    @SerializedName("Code")
    val code: String,
    @SerializedName("MonthlyAveragePrice")
    val monthlyAveragePrice: String,
    @SerializedName("Name")
    val name: String
)