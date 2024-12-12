package com.example.data.model

import com.example.api.models.ArticleDTO
import com.example.api.models.SourceDTO
import com.example.database.models.ArticleDBO
import com.example.database.models.SourceDBO

internal fun ArticleDBO.toArticle(): Article {
    return Article(
        cacheId = id,
        source = source?.toSource(),
        author = author,
        title = title,
        description = description,
        url = url,
        urlToImage = urlToImage,
        publishedAt = publishedAt,
        content = content
    )
}

internal fun ArticleDTO.toArticleDBO(): ArticleDBO {
    return ArticleDBO(
        source = source?.toSourceDBO(),
        author = author,
        title = title,
        description = description,
        url = url,
        urlToImage = urlToImage,
        publishedAt = publishedAt,
        content = content
    )
}

internal fun ArticleDTO.toArticle(): Article {
    return Article(
        source = source?.toSource(),
        author = author,
        title = title,
        description = description,
        url = url,
        urlToImage = urlToImage,
        publishedAt = publishedAt,
        content = content
    )
}

private fun SourceDTO.toSourceDBO() = SourceDBO(id = id, name = name)

private fun SourceDTO.toSource() = Source(id = id, name = name)

private fun SourceDBO.toSource() = Source(id = id, name = name)
