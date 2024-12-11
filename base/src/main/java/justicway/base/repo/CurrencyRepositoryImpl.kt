package justicway.base.repo

import justicway.base.hasCase
import justicway.base.nullCase
import justicway.base.pojo.CurrencyBO
import justicway.base.retrofit.api.CurrencyAPI
import justicway.base.retrofit.response.currency.CurrencyCode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import kotlin.reflect.full.memberProperties
import javax.inject.Inject

class CurrencyRepositoryImpl @Inject constructor(
    private val currencyApi: CurrencyAPI
) : CurrencyRepository {

    override suspend fun fetchCurrency(
        apikey: String,
        baseCurrency: String,
        exChangeCurrencies: String?,
    ): CurrencyBO = withContext(Dispatchers.IO) {
        return@withContext CurrencyBO(
            baseCurrencyCode = baseCurrency,
            rates = async { currencyApi.fetchCurrency(apikey, baseCurrency, exChangeCurrencies)}.await().getOrNull()?.data?.toMap() ?: emptyMap()
        )
    }

}
