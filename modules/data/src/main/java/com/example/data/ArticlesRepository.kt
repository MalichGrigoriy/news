package com.example.data

import com.example.api.NewsApi
import com.example.api.models.ArticleDTO
import com.example.api.models.ResponseDTO
import com.example.common.Logger
import com.example.data.model.Article
import com.example.data.model.toArticle
import com.example.data.model.toArticleDBO
import com.example.database.NewsDataBase
import com.example.database.models.ArticleDBO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.withIndex

public class ArticlesRepository(
    private val database: NewsDataBase,
    private val api: NewsApi,
    private val logger: Logger
) {

    private companion object {
        const val LOG_TAG = "ArticlesRepositoryLogTag"
    }

    public fun getAll(
        query: String,
        mergeStrategy: MergeStrategy<RequestResult<List<Article>>> = RequestResponseMergeStrategy()
    ): Flow<RequestResult<List<Article>>> {
        val cachedAllArticles: Flow<RequestResult<List<Article>>> = getCachedAllArticles()
        val remoteArticles: Flow<RequestResult<List<Article>>> = getRemoteArticles(query)

        return cachedAllArticles.combine(remoteArticles, mergeStrategy::merge)
            .filter { (it is RequestResult.Ignore).not() }
            .flatMapLatest { result ->
                if (result is RequestResult.Success) {
                    database.articlesDao.observeAll()
                        .map { dbos: List<ArticleDBO> -> dbos.map { it.toArticle() } }
                        .map { RequestResult.Success(it) }
                } else {
                    flowOf(result)
                }
            }
    }

    private fun getRemoteArticles(query: String): Flow<RequestResult<List<Article>>> {
        return flow {
            emit(RequestResult.InProgress(null))
            emit(api.everything(query = query).toRequestResult())
        }
            .map { it.copy { resp: ResponseDTO<ArticleDTO> -> resp.articles } }
            .onEach { result ->
                if (result is RequestResult.Success) {
                    saveCache(result.data)
                } else if (result is RequestResult.Error) {
                    logger.e(LOG_TAG, "Error getting from server = ", result.error)
                }
            }
            .map { it.copy { data: List<ArticleDTO> -> data.map { it.toArticle() } } }
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
            .withIndex()
            .map { indexValue ->
                when (indexValue.index) {
                    0 -> RequestResult.InProgress(indexValue.value)
                    else -> RequestResult.Success(indexValue.value)
                }
            }.catch { error: Throwable ->
                RequestResult.Error<List<Article>>(error = error)
                logger.e(LOG_TAG, "Error getting from database = ", error)
            }
    }

    @Suppress("UnusedParameter")
    public suspend fun search(query: String): Flow<Article> {
        TODO()
    }
}

public sealed class RequestResult<out E : Any>(public open val data: E? = null) {
    public class InProgress<E : Any>(data: E?) : RequestResult<E>(data)
    public class Success<E : Any>(override val data: E) : RequestResult<E>(data)
    public class Error<E : Any>(data: E? = null, public val error: Throwable? = null) : RequestResult<E>(data)
    public class Ignore<E : Any> : RequestResult<E>()
}

public fun <I : Any, O : Any> RequestResult<I>.copy(mapper: (I) -> O): RequestResult<O> {
    return when (this) {
        is RequestResult.Error -> RequestResult.Error(data = data?.let(mapper), error = error)
        is RequestResult.InProgress -> RequestResult.InProgress(data = data?.let(mapper))
        is RequestResult.Success -> RequestResult.Success(mapper(data))
        is RequestResult.Ignore -> RequestResult.Ignore()
    }
}

private fun <T : Any> Result<T>.toRequestResult(): RequestResult<T> {
    return when {
        isSuccess -> RequestResult.Success(getOrThrow())
        isFailure -> RequestResult.Error<T>(error = this.exceptionOrNull())
        else -> error(" ")
    }
}
