package com.example.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.database.dao.ArticleDAO
import com.example.database.models.ArticleDBO

class NewsDataBase internal constructor(private val dataBase: NewsRoomDataBase) {
    val articlesDao: ArticleDAO
        get() = dataBase.articlesDao()
}

@Database(entities = [ArticleDBO::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
internal abstract class NewsRoomDataBase : RoomDatabase() {
    abstract fun articlesDao(): ArticleDAO
}

fun createNewsDataBase(applicationContext: Context): NewsDataBase {
    val newsRoomDataBase = Room.databaseBuilder(
        checkNotNull(applicationContext.applicationContext),
        NewsRoomDataBase::class.java,
        "news"
    ).build()
    return NewsDataBase(newsRoomDataBase)
}
