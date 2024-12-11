package justicway.base.repo

import justicway.base.pojo.StockDailyInfoBO

/**
 * Taiwan Stock
 */
interface TaiwanStockRepository {

    suspend fun fetchStockDailyInfo(
    ): List<StockDailyInfoBO>

}