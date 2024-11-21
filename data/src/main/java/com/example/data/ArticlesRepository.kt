package com.example.data

import com.example.data.model.Article
import com.example.data.model.toArticle
import com.example.data.model.toArticleDBO
import com.example.data.model.toArticleDTO
import com.example.database.NewsDataBase
import com.example.database.models.ArticleDBO
import com.example.newsapi.NewsApi
import com.example.newsapi.models.ArticleDTO
import com.example.newsapi.models.ResponseDTO
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart

class ArticlesRepository @Inject constructor(
    private val database: NewsDataBase,
    private val api: NewsApi
) {
    fun getAll(
        mergeStrategy: RequestResponseMergeStrategy<List<Article>> = RequestResponseMergeStrategy()
    ): Flow<RequestResult<List<Article>>> {

        val cachedAllArticles: Flow<RequestResult<List<Article>>> = getCachedAllArticles()
        val remoteArticles: Flow<RequestResult<List<Article>>> = getRemoteArticles()

        return cachedAllArticles.combine(remoteArticles, mergeStrategy::merge)
            .filter { (it is RequestResult.Ignore).not() }
            .flatMapLatest { result ->
                if (result is RequestResult.Success){
                    database.articlesDao.observeAll()
                        .map { dbos: List<ArticleDBO> -> dbos.map { it.toArticle() } }
                        .map { RequestResult.Success(it) }
                } else {
                    flowOf(result)
                }
            }
    }

    private fun getRemoteArticles(): Flow<RequestResult<List<Article>>> {
        return flow { emit(api.everything()) }
            .map { it.toRequestResult() }
            .map { result: RequestResult<ResponseDTO<ArticleDTO>> ->
                result.copy { it: ResponseDTO<ArticleDTO> ->
                    it.articles.map { articlesDTO -> articlesDTO.toArticle() }
                }
            }
            .onEach { result ->
                if (result is RequestResult.Success) {
                    saveCache( result.data.map { it.toArticleDTO() })
                }
            }
            .onStart { RequestResult.InProgress(null) }
    }

    private suspend fun saveCache(data: List<ArticleDTO>) {
        data.map { it.toArticleDBO() }.let {
            database.articlesDao.insert(it)
        }
    }

    private fun getCachedAllArticles(): Flow<RequestResult<List<Article>>> {
        return database.articlesDao
            .getAll()
            .map { articles -> articles.map { it.toArticle() } }
            .map { articles -> RequestResult.Success(articles) }
    }


    suspend fun search(query: String): Flow<Article> {
        TODO()
    }
}

sealed class RequestResult<out E: Any>(open val data: E? = null) {
    class InProgress<E: Any>(data: E?) : RequestResult<E>(data)
    class Success<E: Any>(override val data: E) : RequestResult<E>(data)
    class Error<E: Any>(data: E?, val error: kotlin.Error? = null) : RequestResult<E>(data)
    class Ignore<E: Any> : RequestResult<E>()
}

fun <I: Any, O: Any> RequestResult<I>.copy(mapper: (I) -> O): RequestResult<O> {
    return when (this) {
        is RequestResult.Error -> RequestResult.Error(data = data?.let(mapper))
        is RequestResult.InProgress -> RequestResult.InProgress(data = data?.let(mapper))
        is RequestResult.Success -> RequestResult.Success(mapper(data))
        is RequestResult.Ignore -> RequestResult.Ignore()
    }
}

internal fun <T: Any> Result<T>.toRequestResult(): RequestResult<T> {
    return when {
        isSuccess -> RequestResult.Success(getOrThrow())
        isFailure -> RequestResult.Error<T>(null)
        else -> error(" ")
    }
}
