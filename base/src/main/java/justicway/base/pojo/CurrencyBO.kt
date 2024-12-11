package justicway.base.pojo

data class CurrencyBO(
    val baseCurrencyCode: String,
    val rates: Map<String, Double>

)

