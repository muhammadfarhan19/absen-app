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
    private const val BASE_URL = "http://10.0.2.2:3000/" // Match backend root path

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
                // In a real app, you would also trigger a navigation to Login
            }
            
            response
        }

        val client = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor(authInterceptor)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()

        return retrofit.create(ApiService::class.java)
    }
}
