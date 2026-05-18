package com.app.payroll.data.remote.api

import com.app.payroll.storage.AuthDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object NetworkConfig {
    private const val DEFAULT_BASE_URL = "https://enormously-epic-eft.ngrok-free.app"

    fun getApiService(authDataStore: AuthDataStore): ApiService {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val authInterceptor = Interceptor { chain ->
            val token = runBlocking { authDataStore.token.first() }
            val request = chain.request().newBuilder()
            if (token != null) {
                request.addHeader("Authorization", "Bearer $token")
            }
            
            val response = chain.proceed(request.build())
            
            if (response.code == 401) {
                runBlocking { authDataStore.clearAuth() }
            }
            
            response
        }

        val client = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor(authInterceptor)
            .build()

        val savedBaseUrl = runBlocking { authDataStore.baseUrl.first() }
        val baseUrl = if (!savedBaseUrl.isNullOrBlank()) {
            // Ensure URL ends with /
            if (savedBaseUrl.endsWith("/")) savedBaseUrl else "$savedBaseUrl/"
        } else {
            DEFAULT_BASE_URL
        }

        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()

        return retrofit.create(ApiService::class.java)
    }
}
