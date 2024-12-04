package com.example.data.model

import java.util.Date

const val ID_NONE: Long = 0L

data class Article(
    var cacheId: Long = ID_NONE,
    var source: Source?,
    var author: String?,
    var title: String?,
    var description: String?,
    var url: String?,
    var urlToImage: String?,
    var publishedAt: Date?,
    var content: String?
)
