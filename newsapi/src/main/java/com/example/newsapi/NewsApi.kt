package com.example.newsapi

import retrofit2.http.GET
import retrofit2.http.Query
import java.util.Date
import androidx.annotation.IntRange
import com.example.newsapi.models.ArticleDTO
import com.example.newsapi.models.LanguageDTO
import com.example.newsapi.models.ResponseDTO
import com.example.newsapi.models.SortByDTO
import com.example.newsapi.util.ApiKeyInterceptor
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.skydoves.retrofit.adapters.result.ResultCallAdapterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.create

interface NewsApi {

    @GET("/everything")
    suspend fun everything(
        @Query("q") query: String? = null,
        @Query("from") from: Date? = null,
        @Query("to") to: Date? = null,
        @Query("languages") languages: List<LanguageDTO>? = null,
        @Query("sortBy") sortBy: SortByDTO? = null,
        @Query("pageSize") @IntRange(from = 0, to = 100) pageSize: Int = 100,
        @Query("page") @IntRange(from = 1) page: Int = 1,
    ): Result<ResponseDTO<ArticleDTO>> //todo
}

fun NewsApi(
    baseUrl: String,
    apiKey: String,
    okHttpClient: OkHttpClient? = null,
): NewsApi {
    val retrofit = retrofit(baseUrl, apiKey, okHttpClient)
    return retrofit.create()
}

internal fun retrofit(
    baseUrl: String,
    apiKey: String,
    okHttpClient: OkHttpClient? = null,
): Retrofit {
    val contentType = MediaType.get("application/json")
    val httpClient = (okHttpClient?.newBuilder() ?: OkHttpClient.Builder())
        .addInterceptor(ApiKeyInterceptor(apiKey))
        .build()

    return Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(Json.asConverterFactory(contentType))
        .addCallAdapterFactory(ResultCallAdapterFactory.create())
        .client(httpClient)
        .build()
}
