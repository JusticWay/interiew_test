package justicway.base.pojo

import com.google.gson.annotations.SerializedName

data class StockDailyInfoBO(
    val code: String,
    val change: Double,
    val openingPrice: Double,
    val highestPrice: Double,
    val lowestPrice: Double,
    val closingPrice: Double,
    val name: String,
    val tradeValue: Long,
    val tradeVolume: Long,
    val transaction: Long,
    //  bbu
    val dividendYield: Double,
    val pbRatio: Double,
    val peRatio: Double,
    // avg
    val monthlyAveragePrice: Double,
){
    fun isOverMonthlyAverage(
        price: Double = closingPrice,
    ): Boolean {
        return try{
            price > monthlyAveragePrice
        } catch (e: NumberFormatException){
            false
        }
    }
    fun isLowerMonthlyAverage(
        price: Double = closingPrice,
    ): Boolean {
        return try{
            price < monthlyAveragePrice
        } catch (e: NumberFormatException){
            false
        }
    }
}
