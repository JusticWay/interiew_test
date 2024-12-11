package justicway.base.pojo

import com.google.gson.annotations.SerializedName

data class StockDailyInfoBO(
    val code: String,
    val change: String,
    val closingPrice: String,
    val highestPrice: String,
    val lowestPrice: String,
    val name: String,
    val openingPrice: String,
    val tradeValue: String,
    val tradeVolume: String,
    val transaction: String,
    //  bbu
    val dividendYield: String,
    val pbRatio: String,
    val peRatio: String,
    // avg
    val monthlyAveragePrice: String

)
