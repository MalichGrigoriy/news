package com.example.data

import com.example.data.model.Article
import com.example.data.model.toArticle
import com.example.data.model.toArticleDBO
import com.example.database.NewsDataBase
import com.example.api.NewsApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart

class ArticlesRepositoryGreg(
    private val database: NewsDataBase,
    private val api: NewsApi,
) {
    fun getAll(
        mergeStrategy: RequestResponseMergeStrategy<Any> = RequestResponseMergeStrategy()
    ): Flow<RequestResult<List<Article>>> {

        val cached: Flow<List<Article>> = database.articlesDao
            .getAll()
            .map { list -> list.map { art -> art.toArticle() } }

        val remote: Flow<RequestResult<*>> = flow {
            emit(api.everything())
        }.map { response ->
            if (response.isSuccess) {
                val articleDBO = response.getOrThrow().articles.map { it.toArticleDBO() }
                database.articlesDao.insert(articleDBO)
                RequestResult.Success(articleDBO)
            } else {
                RequestResult.Error(null)
            }
        }.onStart { emit(RequestResult.InProgress(null)) }

        return remote.combineResultData(cached).debounce(250)
    }

    suspend fun search(query: String): Flow<Article> {
        TODO()
    }
}
