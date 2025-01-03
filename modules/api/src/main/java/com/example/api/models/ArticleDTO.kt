package com.example.api.models

import com.example.api.util.DateSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.Date

@Serializable
data class ArticleDTO(

    @SerialName("source") var source: SourceDTO? = SourceDTO(),
    @SerialName("author") var author: String? = null,
    @SerialName("title") var title: String? = null,
    @SerialName("description") var description: String? = null,
    @SerialName("url") var url: String? = null,
    @SerialName("urlToImage") var urlToImage: String? = null,

    @SerialName("publishedAt")
    @Serializable(with = DateSerializer::class)
    var publishedAt: Date,

    @SerialName("content") var content: String? = null

)
