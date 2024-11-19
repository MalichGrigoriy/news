package com.example.data.model

import java.util.Date

data class Article(
    var author: String? = null,
    var title: String? = null,
    var description: String? = null,
    var url: String? = null,
    var urlToImage: String? = null,
    var publishedAt: Date,
    var content: String? = null
)