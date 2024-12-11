package justicway.base.retrofit.interceptor

import okhttp3.Request
import okhttp3.ResponseBody
import okio.Timeout
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

private inline fun <reified T> tryCast(instance: Any?): Boolean = instance is T

class ResultCall<T>(private val delegate: Call<T>) :
    Call<Result<T>> {

    companion object {
        const val RETRY_TIME = 3
    }

    private var retryCount = 1

    private fun isRetryOver(): Boolean = (retryCount == RETRY_TIME)

    override fun enqueue(callback: Callback<Result<T>>) {
        delegate.enqueue(
            object : Callback<T> {
                override fun onResponse(call: Call<T>, response: Response<T>) {
                    if (response.isSuccessful) {
                        response.body()?.let { body ->
                            callback.onResponse(
                                this@ResultCall,
                                Response.success(
                                    response.code(),
                                    Result.success(body)
                                )
                            )
                        } ?: run {
                            if (tryCast<Response<Unit>>(response)) {
                                callback.onResponse(
                                    this@ResultCall,
                                    Response.success(
                                        response.code(),
                                        Result.success(Unit as T)
                                    )
                                )
                            } else {
                                if (call.request().method != "GET" || isRetryOver()) {
                                    callback.onResponse(
                                        this@ResultCall,
                                        Response.success(Result.failure(parseErrorBody(response)))
                                    )
                                } else {
                                    retry(callback, call)
                                }
                            }
                        }
                    } else {
                        if (response.code() == ApiError.UNAUTHORIZED) {
                            callback.onResponse(
                                this@ResultCall,
                                Response.success(Result.failure(parseErrorBody(response)))
                            )
                        } else {
                            if (call.request().method != "GET" || isRetryOver()) {
                                callback.onResponse(
                                    this@ResultCall,
                                    Response.success(Result.failure(parseErrorBody(response)))
                                )
                            } else {
                                retry(callback, call)
                            }
                        }
                    }
                }

                override fun onFailure(call: Call<T>, t: Throwable) {
                    val code = when (t) {
                        is HttpException -> t.code()
                        is IOException -> ApiError.ROUTER_NOT_FOUND
                        else -> ApiError.UNKNOWN
                    }

                    if (code == ApiError.UNAUTHORIZED) {
                        callback.onResponse(
                            this@ResultCall,
                            Response.success(Result.failure(ApiError(ApiError.UNKNOWN, code)))
                        )
                    } else {
                        if (call.request().method != "GET" || isRetryOver()) {
                            callback.onResponse(
                                this@ResultCall,
                                Response.success(Result.failure(ApiError(ApiError.UNKNOWN, code)))
                            )
                        } else {
                            retry(callback, call)
                        }
                    }
                }
            }
        )
    }

    private fun retry(callback: Callback<Result<T>>, retryCall: Call<T>) {
        retryCount++
        retryCall.clone().enqueue(object : Callback<T> {
            override fun onResponse(call: Call<T>, response: Response<T>) {
                if (response.isSuccessful) {
                    callback.onResponse(
                        this@ResultCall,
                        Response.success(
                            response.code(),
                            Result.success(response.body() ?: Unit as T)
                        )
                    )
                } else {
                    if (response.code() == ApiError.UNAUTHORIZED) {
                        callback.onResponse(
                            this@ResultCall,
                            Response.success(Result.failure(parseErrorBody(response)))
                        )
                    } else {
                        if ( call.request().method != "GET" || isRetryOver()) {
                            callback.onResponse(
                                this@ResultCall,
                                Response.success(Result.failure(parseErrorBody(response)))
                            )
                        } else {
                            retry(callback, call)
                        }
                    }
                }
            }

            override fun onFailure(call: Call<T>, t: Throwable) {
                val code = when (t) {
                    is HttpException -> t.code()
                    is IOException -> ApiError.ROUTER_NOT_FOUND
                    else -> ApiError.UNKNOWN
                }

                if (code == ApiError.UNAUTHORIZED) {
                    callback.onResponse(
                        this@ResultCall,
                        Response.success(Result.failure(ApiError(ApiError.UNKNOWN, code)))
                    )
                } else {
                    if (call.request().method != "GET" || isRetryOver()) {
                        callback.onResponse(
                            this@ResultCall,
                            Response.success(Result.failure(ApiError(ApiError.UNKNOWN, code)))
                        )
                    } else {
                        retry(callback, call)
                    }
                }
            }
        })
    }

    private fun parseErrorBody(response: Response<T>): Throwable {
        return kotlin.runCatching {
            response.errorBody().apiError(response.code())
        }.getOrElse {
            HttpException(response)
        }
    }

    override fun isExecuted(): Boolean {
        return delegate.isExecuted
    }

    override fun execute(): Response<Result<T>> {
        return Response.success(Result.success(delegate.execute().body()!!))
    }

    override fun cancel() {
        delegate.cancel()
    }

    override fun isCanceled(): Boolean {
        return delegate.isCanceled
    }

    override fun clone(): Call<Result<T>> {
        return ResultCall(delegate.clone())
    }

    override fun request(): Request {
        return delegate.request()
    }

    override fun timeout(): Timeout {
        return delegate.timeout()
    }
}

fun ResponseBody?.apiError(httpCode: Int): ApiError {
    val errorMessage = this?.string() ?: ""
    return try {
        val json = JSONObject(errorMessage)
        val code = when {
            json.has("code") -> {
                json.getInt("code")
            }

            json.has("status") -> {
                json.getInt("status")
            }

            else -> {
                ApiError.JSON_EXCEPTION
            }
        }
        return ApiError(httpCode = httpCode, code = code, message = json.getString("message"))
    } catch (e: JSONException) {
        ApiError(httpCode = httpCode, code = ApiError.JSON_EXCEPTION, message = e.localizedMessage)
    } catch (e1: Exception) {
        ApiError(httpCode = httpCode, code = ApiError.UNKNOWN, message = e1.localizedMessage)
    }
}