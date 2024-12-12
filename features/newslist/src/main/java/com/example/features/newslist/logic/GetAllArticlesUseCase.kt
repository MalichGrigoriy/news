package com.example.features.newslist.logic

import com.example.data.ArticlesRepository
import com.example.data.RequestResult
import com.example.data.copy
import com.example.data.model.Article
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

internal class GetAllArticlesUseCase @Inject constructor(private val repository: ArticlesRepository) {

    operator fun invoke(query: String): Flow<RequestResult<List<ArticleUI>>> {
        return repository.getAll(query).map { requestResult: RequestResult<List<Article>> ->
            requestResult.copy { article: List<Article> ->
                article.map { it.toArticleUI() }
            }
        }
    }

    fun fetchLatest() {
        // TODO repository.fetchLatest()
    }
}
