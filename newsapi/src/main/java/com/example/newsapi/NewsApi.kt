package com.example.newsapi

import androidx.annotation.IntRange
import com.example.newsapi.models.ArticleDTO
import com.example.newsapi.models.LanguageDTO
import com.example.newsapi.models.ResponseDTO
import com.example.newsapi.models.SortByDTO
import com.example.newsapi.util.ApiKeyInterceptor
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.skydoves.retrofit.adapters.result.ResultCallAdapterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.create
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.Date


interface NewsApi {

    @GET("everything")
    suspend fun everything(
        @Query("q") query: String? = null,
        @Query("from") from: Date? = null,
        @Query("to") to: Date? = null,
        @Query("languages") languages: List<@JvmSuppressWildcards LanguageDTO>? = null,
        @Query("sortBy") sortBy: SortByDTO? = null,
        @Query("pageSize") @IntRange(from = 0, to = 100) pageSize: Int = 100,
        @Query("page") @IntRange(from = 1) page: Int = 1,
    ): Result<ResponseDTO<ArticleDTO>> //todo
}

fun NewsApi(
    baseUrl: String,
    apiKey: String,
    okHttpClient: OkHttpClient,
    json: Json = Json,
): NewsApi {
    val retrofit = retrofit(baseUrl, apiKey, okHttpClient, json)
    return retrofit.create()
}

internal fun retrofit(
    baseUrl: String,
    apiKey: String,
    okHttpClient: OkHttpClient,
    json: Json,
): Retrofit {

    val contentType = "application/json".toMediaType()

    val modifiedOkHttpClient =
        okHttpClient.newBuilder()
            .addInterceptor(ApiKeyInterceptor(apiKey))
            .build()

    return Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(json.asConverterFactory(contentType))
        .addCallAdapterFactory(ResultCallAdapterFactory.create())
        .client(modifiedOkHttpClient)
        .build()
}
