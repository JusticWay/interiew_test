package justicway.base.retrofit.di

import justicway.base.BuildConfig

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import justicway.base.retrofit.api.CurrencyAPI
import justicway.base.retrofit.api.TaiwanFutureAPI
import justicway.base.retrofit.api.TaiwanStockAPI
import justicway.base.retrofit.api.TdxAPI
import justicway.base.retrofit.interceptor.AuthInterceptor
import justicway.base.retrofit.interceptor.EnumConverterFactory
import justicway.base.retrofit.interceptor.EnumWithFallbackValueTypeAdapterFactory
import justicway.base.retrofit.interceptor.ResultCallAdapterFactory
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class RetrofitProvideModule {

    @Singleton
    @Provides
    @Named("currency")
    fun provideCurrencyRetrofitClient(
        authInterceptor: AuthInterceptor
    ): Retrofit {
        val baseUrl: String = BuildConfig.HOST_CURRENCY
        val gson: Gson = GsonBuilder().registerTypeAdapterFactory(
            EnumWithFallbackValueTypeAdapterFactory
        ).create()

        val clientBuilder = OkHttpClient
            .Builder()
            .addInterceptor(authInterceptor)


        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addConverterFactory(EnumConverterFactory())
            .addCallAdapterFactory(ResultCallAdapterFactory())
            .client(clientBuilder.build())
            .build()

    }

    @Singleton
    @Provides
    @Named("tdx")
    fun provideTdxRetrofitClient(
        authInterceptor: AuthInterceptor
    ): Retrofit {
        val baseUrl: String = BuildConfig.HOST_TDX
        val gson: Gson = GsonBuilder().registerTypeAdapterFactory(
            EnumWithFallbackValueTypeAdapterFactory
        ).create()

        val clientBuilder = OkHttpClient
            .Builder()
            .addInterceptor(authInterceptor)


        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addConverterFactory(EnumConverterFactory())
            .addCallAdapterFactory(ResultCallAdapterFactory())
            .client(clientBuilder.build())
            .build()

    }

    @Singleton
    @Provides
    @Named("taifex")
    fun provideTaifexRetrofitClient(
        authInterceptor: AuthInterceptor
    ): Retrofit {
        val baseUrl: String = BuildConfig.HOST_TWIFEX
        val gson: Gson = GsonBuilder().registerTypeAdapterFactory(
            EnumWithFallbackValueTypeAdapterFactory
        ).create()

        val clientBuilder = OkHttpClient
            .Builder()
            .addInterceptor(authInterceptor)

        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addConverterFactory(EnumConverterFactory())
            .addCallAdapterFactory(ResultCallAdapterFactory())
            .client(clientBuilder.build())
            .build()

    }

    @Singleton
    @Provides
    @Named("twse")
    fun provideTwseRetrofitClient(
        authInterceptor: AuthInterceptor
    ): Retrofit {
        val baseUrl: String = BuildConfig.HOST_TWSE
        val gson: Gson = GsonBuilder().registerTypeAdapterFactory(
            EnumWithFallbackValueTypeAdapterFactory
        ).create()

        val clientBuilder = OkHttpClient
            .Builder()
            .addInterceptor(authInterceptor)

        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addConverterFactory(EnumConverterFactory())
            .addCallAdapterFactory(ResultCallAdapterFactory())
            .client(clientBuilder.build())
            .build()

    }


    @Singleton
    @Provides
    fun provideTdxApi(@Named("tdx") retrofit: Retrofit): TdxAPI = retrofit.create(TdxAPI::class.java)

    @Singleton
    @Provides
    fun provideCurrencyApi(@Named("currency") retrofit: Retrofit): CurrencyAPI = retrofit.create(CurrencyAPI::class.java)

    @Singleton
    @Provides
    fun provideFeatureApi(@Named("twse") retrofit: Retrofit): TaiwanStockAPI = retrofit.create(TaiwanStockAPI::class.java)

    @Singleton
    @Provides
    fun provideStockApi(@Named("twifex") retrofit: Retrofit): TaiwanFutureAPI = retrofit.create(TaiwanFutureAPI::class.java)


}