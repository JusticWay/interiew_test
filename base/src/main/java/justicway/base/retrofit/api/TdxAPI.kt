package justicway.base.retrofit.api

import justicway.base.retrofit.response.tdx.FetchAirFlightArrivalResponse
import justicway.base.retrofit.response.tdx.FetchAirFlightDepartureResponse
import retrofit2.http.GET
import retrofit2.http.Query

/**
 *
 * https://tdx.transportdata.tw/api-service/swagger#/Air
 *
 */
interface TdxAPI {

    @GET("/v2/Air/FIDS/Airport/Departure")
    suspend fun fetchAirportDeparture(
        @Query("IsCargo") isCargo: Boolean = false,
        @Query("top") top: Int,
        @Query("skip") skip: String?,
        @Query("format") format: String = "json",
    ): Result<List<FetchAirFlightDepartureResponse>>


    @GET("/v2/Air/FIDS/Airport/Arrival")
    suspend fun fetchAirportArrival(
        @Query("IsCargo") isCargo: Boolean = false,
        @Query("top") top: Int,
        @Query("skip") skip: String?,
        @Query("format") format: String = "json",
    ): Result<List<FetchAirFlightArrivalResponse>>


}