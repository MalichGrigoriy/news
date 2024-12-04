package com.example.newssearch

import android.content.Context
import com.example.api.NewsApi
import com.example.common.AppDispatchers
import com.example.common.Logger
import com.example.common.androidLogCatLogger
import com.example.database.NewsDataBase
import com.example.database.createNewsDataBase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideNewsApi(okHttpClient: OkHttpClient): NewsApi {
        // todo or add to dagger graph
        return NewsApi(
            baseUrl = BuildConfig.NEWS_API_BASE_URL,
            apiKey = BuildConfig.NEWS_API_KEY,
            okHttpClient = okHttpClient
        )
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
    fun provideLogger(): Logger = androidLogCatLogger()
}
