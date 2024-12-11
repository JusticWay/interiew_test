package justicway.base.retrofit.interceptor

import androidx.annotation.Keep

/**
 * @param httpCode : Http response Code
 * @param code : Backend response Code
 * @param message : Backend debug message
 */
@Keep
data class ApiError(val httpCode : Int = UNKNOWN ,val code : Int = UNKNOWN , override val message : String = ""): Error() {
    companion object {
        const val UNKNOWN = Int.MAX_VALUE
        // Gateway has response
        const val JSON_EXCEPTION = Int.MAX_VALUE -1
        const val UNAUTHORIZED = 401
        const val ROUTER_NOT_FOUND = 404
    }
}
