package ir.miare.androidcodechallenge.di

import android.content.Context
import android.util.Log
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ir.logicbase.mockfit.MockFitConfig
import ir.logicbase.mockfit.MockFitInterceptor
import ir.miare.androidcodechallenge.data.remote.api.PlayerApiService
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private const val BASE_URL = "https://test_baseurl.com/v2/"

    @Provides
    @Singleton
    fun provideOkHttpClient(@ApplicationContext context: Context): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(
                MockFitInterceptor(
                    bodyFactory = { input -> context.assets.open(input) },
                    logger = { tag, message -> Log.d(tag, message) },
                    baseUrl = BASE_URL,
                    requestPathToJsonMap = MockFitConfig.REQUEST_TO_JSON,
                    mockFilesPath = "",
                    mockFitEnable = true,
                    apiEnableMock = true,
                    apiResponseLatency = 1000L
                )
            )
            .connectTimeout(20, TimeUnit.SECONDS)
            .writeTimeout(20, TimeUnit.SECONDS)
            .readTimeout(20, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(JacksonConverterFactory.create())
            .client(okHttpClient)
            .build()
    }

    @Provides
    @Singleton
    fun providePlayerApiService(retrofit: Retrofit): PlayerApiService {
        return retrofit.create(PlayerApiService::class.java)
    }
}
