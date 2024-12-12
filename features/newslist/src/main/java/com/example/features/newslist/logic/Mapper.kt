package com.example.features.newslist.logic

import com.example.data.RequestResult
import com.example.data.model.Article

internal fun Article.toArticleUI(): ArticleUI = ArticleUI(
    id = this.cacheId,
    title = this.title,
    description = this.description,
    imageUrl = this.urlToImage,
    url = this.url,
)

internal fun RequestResult<List<ArticleUI>>.toSate(): State {
    return when (this) {
        is RequestResult.Error -> State.Error(this.data)
        is RequestResult.InProgress -> State.Loading(this.data)
        is RequestResult.Success -> State.Success(this.data)
        is RequestResult.Ignore -> error(" ")
    }
}
