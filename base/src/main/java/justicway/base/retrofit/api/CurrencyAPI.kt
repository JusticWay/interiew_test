package justicway.base.retrofit.api

import justicway.base.retrofit.response.currency.FetchCurrencyResponse
import retrofit2.http.GET
import retrofit2.http.Query

/**
 *
 * [https://openapi.taifex.com.tw]
 *
 */
interface CurrencyAPI {

    @GET("/v1/latest") // Create battle
    suspend fun fetchCurrency(
        @Query("apikey") apikey: String,
        @Query("base_currency") baseCurrency: String?,
        @Query("currencies") exChangeCurrencies: String?,
    ): Result<FetchCurrencyResponse>

}