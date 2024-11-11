package com.example.database.models

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "articles")
data class ArticleDBO(
    @PrimaryKey (autoGenerate = true) val id: Long,
    @Embedded(prefix = "source.") var source: SourceDBO? = SourceDBO(),
    @ColumnInfo("author") var author: String? = null,
    @ColumnInfo("title") var title: String? = null,
    @ColumnInfo("description") var description: String? = null,
    @ColumnInfo("url") var url: String? = null,
    @ColumnInfo("urlToImage") var urlToImage: String? = null,
    @ColumnInfo("publishedAt") var publishedAt: Date,
    @ColumnInfo("content") var content: String? = null
)