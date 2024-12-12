package justicway.base.retrofit.api

import justicway.base.retrofit.response.twse.FetchBWIBBUResponse
import justicway.base.retrofit.response.twse.FetchStockDailyAvgResponse
import justicway.base.retrofit.response.twse.FetchStockDailyResponse
import retrofit2.http.GET

/**
 *
 * [https://openapi.twse.com.tw]
 *
 */
interface TaiwanStockAPI {

    @GET("v1/exchangeReport/BWIBBU_ALL") //上市個股日本益比、殖利率及股價淨值比（依代碼查詢）
    suspend fun fetchBWIBBU_ALL(
    ): Result<List<FetchBWIBBUResponse>>

    @GET("v1/exchangeReport/STOCK_DAY_AVG_ALL") // 上市個股日收盤價及月平均價
    suspend fun fetchStockDailyAvg(
    ): Result<List<FetchStockDailyAvgResponse>>

    @GET("v1/exchangeReport/STOCK_DAY_ALL") // 上市個股日成交資訊
    suspend fun fetchStockDaily(
    ): Result<List<FetchStockDailyResponse>>


}