package justicway.base.pojo

import com.google.gson.annotations.SerializedName

data class AirFlightBO(
    val type : AirFlightType,
    // General info
    val acType: String,
    val airRouteType: Int,
    val airlineID: String,
    val apron: String,
    val arrivalAirportID: String,
    val baggageClaim: String,
    val checkCounter: String,
    val codeShare: String,
    val departureAirportID: String,
    val flightDate: String,
    val flightNumber: String,
    val gate: String,
    val isCargo: Boolean,
    val terminal: String,
    val updateTime: String,

    // Arrival
    val actualArrivalTime: String?,
    val arrivalRemark: String?,
    val arrivalRemarkEn: String?,
    val estimatedArrivalTime: String?,
    val scheduleArrivalTime: String?,

    // Departure
    val actualDepartureTime: String?,
    val departureRemark: String?,
    val departureRemarkEn: String?,
    val estimatedDepartureTime: String?,
    val scheduleDepartureTime: String?,
)

enum class AirFlightType {
    Arrival,
    Departure
}