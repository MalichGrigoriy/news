package com.example.newssearch

import android.content.Context
import com.example.database.NewsDataBase
import com.example.database.createNewsDataBase
import com.example.news_common.AndroidLogCatLogger
import com.example.news_common.AppDispatchers
import com.example.news_common.Logger
import com.example.newsapi.NewsApi
import com.example.newsapi.util.ApiKeyInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideNewsApi(okHttpClient: OkHttpClient): NewsApi {

        //todo or add to dagger graph
        return NewsApi(
            baseUrl = BuildConfig.NEWS_API_BASE_URL,
            apiKey = BuildConfig.NEWS_API_KEY,
            okHttpClient = okHttpClient
        )
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val logging = if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        } else null

        val httpBuilder = OkHttpClient.Builder()
            .addInterceptor(ApiKeyInterceptor(BuildConfig.NEWS_API_KEY)) //todo move from other module
        logging?.let { httpBuilder.addInterceptor(logging) }

        return httpBuilder.build()
    }

    @Provides
    @Singleton
    fun provideNewsDataBase(@ApplicationContext context: Context): NewsDataBase {
        return createNewsDataBase(context)
    }

    @Provides
    @Singleton
    fun provideDispatcher(): AppDispatchers = AppDispatchers()

    @Provides
    fun provideLogger(): Logger = AndroidLogCatLogger()


}