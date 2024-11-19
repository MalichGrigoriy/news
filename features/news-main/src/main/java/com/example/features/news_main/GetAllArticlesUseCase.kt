package com.example.features.news_main

import com.example.data.ArticlesRepository
import com.example.data.ArticlesRepositoryGreg
import com.example.data.RequestResult
import com.example.data.copy
import com.example.data.model.Article
import com.example.data.model.toArticleDTO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetAllArticlesUseCase(private val repository: ArticlesRepository) {

    operator fun invoke(): Flow<RequestResult<List<ArticleUI>>> {
        return repository.getAll().map { requestResult: RequestResult<List<Article>> ->
            requestResult.copy { article: List<Article> ->
                article.map { it.toArticleUI() }
            }
        }
    }

    fun fetchLatest() {
// TODO       repository.fetchLatest()
    }
}

private fun Article.toArticleUI(): ArticleUI {
    TODO("Not yet implemented")
}

