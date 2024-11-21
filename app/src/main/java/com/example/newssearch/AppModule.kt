package com.example.newssearch

import android.content.Context
import com.example.data.ArticlesRepository
import com.example.database.NewsDataBase
import com.example.database.createNewsDataBase
import com.example.newsapi.NewsApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideNewsApi(): NewsApi {
        //or add to dagger graph
        return NewsApi(
            baseUrl = BuildConfig.NEWS_API_BASE_URL,
            apiKey = BuildConfig.NEWS_API_KEY,
        )
    }

    @Provides
    @Singleton
    fun provideNewsDataBase(@ApplicationContext context: Context): NewsDataBase {
        return createNewsDataBase(context)
    }
}