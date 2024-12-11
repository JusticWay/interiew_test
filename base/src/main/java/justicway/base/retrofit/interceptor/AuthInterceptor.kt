package justicway.base.retrofit.interceptor

import android.content.SharedPreferences
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor(private val sharedPreferences: SharedPreferences): Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
//        val token = sharedPreferences.getString("token", "")
        val clientId = ClientConfig.clientId
        val clientSecret = ClientConfig.clientSecret

        val newRequest = chain.request().newBuilder()
//            .addHeader("Authorization","Bearer $token")
            .addHeader("client_id", clientId)
            .addHeader("client_secret", clientSecret)
            .build()

        return chain.proceed(newRequest)
    }
}

data object ClientConfig{
    val clientId : String = "studiolisten-3073851c-e80f-4cbb"
    val clientSecret : String = "bde90741-d7ac-4fa8-9dc9-bd9de5575208"
}