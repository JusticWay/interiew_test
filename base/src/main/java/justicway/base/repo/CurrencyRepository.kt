package justicway.base.repo

import justicway.base.pojo.CurrencyBO
import justicway.base.retrofit.response.currency.FetchCurrencyResponse


interface CurrencyRepository {

    suspend fun fetchCurrency(
        apikey: String,
        baseCurrency: String = "USD",
        exChangeCurrencies: String?,
    ): CurrencyBO

}