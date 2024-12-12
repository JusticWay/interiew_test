package justicway.base.repo

import justicway.base.default
import justicway.base.hasCase
import justicway.base.nullCase
import justicway.base.pojo.StockDailyInfoBO
import justicway.base.retrofit.api.TaiwanStockAPI
import justicway.base.retrofit.response.twse.FetchBWIBBUResponse
import justicway.base.retrofit.response.twse.FetchStockDailyAvgResponse
import justicway.base.retrofit.response.twse.FetchStockDailyResponse
import justicway.base.safeToDouble
import justicway.base.safeToLong
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import javax.inject.Inject

class TaiwanStockRepositoryImpl @Inject constructor(
    private val stockApi: TaiwanStockAPI
) : TaiwanStockRepository {

    var cacheStockInfo : List<StockDailyInfoBO>? = null

    override suspend fun fetchStockDailyInfo(): List<StockDailyInfoBO> =
        withContext(Dispatchers.IO) {
            cacheStockInfo.hasCase {
                return@withContext it
            }

            val bwibbu = async { stockApi.fetchBWIBBU_ALL() }.await().getOrNull() ?: emptyList()
            val avg = async { stockApi.fetchStockDailyAvg() }.await().getOrNull() ?: emptyList()
            val daily = async { stockApi.fetchStockDaily() }.await().getOrNull() ?: emptyList()


            cacheStockInfo = convertResponseToBusinessObject(bwibbu,avg,daily)

            cacheStockInfo.hasCase {
                return@withContext it
            }.nullCase {
                return@withContext emptyList()
            }!!
        }



    private fun convertResponseToBusinessObject(
        bwibbu: List<FetchBWIBBUResponse>,
        avg: List<FetchStockDailyAvgResponse>,
        daily: List<FetchStockDailyResponse>
    ): List<StockDailyInfoBO> {
        return daily.map { dailyItem ->
            val avgItem = avg.find { it.code == dailyItem.code }
            val bwibbuItem = bwibbu.find { it.code == dailyItem.code }
            return@map StockDailyInfoBO(
                code = dailyItem.code,
                name = dailyItem.name,
                openingPrice = dailyItem.openingPrice.safeToDouble(),
                highestPrice = dailyItem.highestPrice.safeToDouble(),
                lowestPrice = dailyItem.lowestPrice.safeToDouble(),
                closingPrice = dailyItem.closingPrice.safeToDouble(),
                change = dailyItem.change.safeToDouble(),
                tradeVolume = dailyItem.tradeVolume.safeToLong(),
                tradeValue = dailyItem.tradeValue.safeToLong(),
                transaction = dailyItem.transaction.safeToLong(),

                monthlyAveragePrice = avgItem?.monthlyAveragePrice.safeToDouble(),

                dividendYield = bwibbuItem?.dividendYield.safeToDouble(),
                peRatio = bwibbuItem?.peRatio.safeToDouble(),
                pbRatio = bwibbuItem?.pbRatio.safeToDouble(),
            )
        }
    }

}