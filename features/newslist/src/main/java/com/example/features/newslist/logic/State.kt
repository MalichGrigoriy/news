package com.example.features.newslist.logic

internal sealed class State(open val articles: List<ArticleUI>?) {
    data object None : State(null)
    class Loading(articles: List<ArticleUI>? = null) : State(articles)
    class Error(articles: List<ArticleUI>? = null) : State(articles)
    class Success(override val articles: List<ArticleUI>) : State(articles)
}
