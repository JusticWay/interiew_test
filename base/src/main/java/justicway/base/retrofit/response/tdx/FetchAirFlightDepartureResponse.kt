package justicway.base.retrofit.response.tdx

import com.google.gson.annotations.SerializedName

data class FetchAirFlightDepartureResponse(
    // General info
    @SerializedName("AcType")
    val acType: String,
    @SerializedName("AirRouteType")
    val airRouteType: Int,
    @SerializedName("AirlineID")
    val airlineID: String,
    @SerializedName("Apron")
    val apron: String,
    @SerializedName("ArrivalAirportID")
    val arrivalAirportID: String,
    @SerializedName("BaggageClaim")
    val baggageClaim: String,
    @SerializedName("CheckCounter")
    val checkCounter: String,
    @SerializedName("CodeShare")
    val codeShare: String,
    @SerializedName("DepartureAirportID")
    val departureAirportID: String,
    @SerializedName("FlightDate")
    val flightDate: String,
    @SerializedName("FlightNumber")
    val flightNumber: String,
    @SerializedName("Gate")
    val gate: String,
    @SerializedName("IsCargo")
    val isCargo: Boolean,
    @SerializedName("Terminal")
    val terminal: String,
    @SerializedName("UpdateTime")
    val updateTime: String,
    // Departure
    @SerializedName("ActualDepartureTime")
    val actualDepartureTime: String?,
    @SerializedName("DepartureRemark")
    val departureRemark: String?,
    @SerializedName("DepartureRemarkEn")
    val departureRemarkEn: String?,
    @SerializedName("EstimatedDepartureTime")
    val estimatedDepartureTime: String?,
    @SerializedName("ScheduleDepartureTime")
    val scheduleDepartureTime: String?,

)