package justicway.base.repo

import justicway.base.hasCase
import justicway.base.nullCase
import justicway.base.pojo.StockDailyInfoBO
import justicway.base.retrofit.api.TaiwanStockAPI
import justicway.base.retrofit.response.twse.FetchBWIBBUResponse
import justicway.base.retrofit.response.twse.FetchStockDailyAvgResponse
import justicway.base.retrofit.response.twse.FetchStockDailyResponse
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
                openingPrice = dailyItem.openingPrice,
                highestPrice = dailyItem.highestPrice,
                lowestPrice = dailyItem.lowestPrice,
                closingPrice = dailyItem.closingPrice,
                change = dailyItem.change,
                tradeVolume = dailyItem.tradeVolume,
                tradeValue = dailyItem.tradeValue,
                transaction = dailyItem.transaction,

                monthlyAveragePrice = avgItem?.monthlyAveragePrice ?: "",

                dividendYield = bwibbuItem?.dividendYield ?: "",
                peRatio = bwibbuItem?.peRatio ?: "",
                pbRatio = bwibbuItem?.pbRatio ?: ""
            )
        }
    }

}