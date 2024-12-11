package justicway.base.repo

import justicway.base.pojo.AirFlightBO

/**
 * TDX Transport Data
 */
interface TransportDataRepository {

    suspend fun fetchAirFlight(
        apikey: String,
        pageSize: Int?,
        skipPage: Int?,
    ): Result<List<AirFlightBO>>

}