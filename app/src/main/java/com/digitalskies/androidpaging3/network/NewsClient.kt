package com.digitalskies.androidpaging3.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object NewsClient {

    const val BASE_URL="https://newsapi.org/v2/"

    const val API_KEY="b2001f141c034d3ebf1425f0818f39d5"

    fun getNewsApi():NewsApi{
        val httpLoggingInterceptor=HttpLoggingInterceptor()

        httpLoggingInterceptor.level=HttpLoggingInterceptor.Level.BODY

        val okHttpClientBuilder=OkHttpClient.Builder()

        okHttpClientBuilder.addInterceptor(httpLoggingInterceptor)

        okHttpClientBuilder.readTimeout(15,TimeUnit.SECONDS)

        okHttpClientBuilder.connectTimeout(15,TimeUnit.SECONDS)

        return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okHttpClientBuilder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(NewsApi::class.java)
    }
}