package com.example.features.domain.newslist

public sealed class State(public open val articles: List<ArticleUI>?) {
    public data object None : State(null)
    public class Loading(articles: List<ArticleUI>? = null) : State(articles)
    public class Error(articles: List<ArticleUI>? = null) : State(articles)
    public class Success(override val articles: List<ArticleUI>) : State(articles)
}
