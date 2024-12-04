package com.example.data

import com.example.data.RequestResult.Error
import com.example.data.RequestResult.Ignore
import com.example.data.RequestResult.InProgress
import com.example.data.RequestResult.Success

public interface MergeStrategy<E> {

    public fun merge(
        right: E,
        left: E
    ): E
}

class RequestResponseMergeStrategy<T : Any> : MergeStrategy<RequestResult<T>> {
    @Suppress("CyclomaticComplexMethod")
    override fun merge(
        local: RequestResult<T>,
        remote: RequestResult<T>
    ): RequestResult<T> {
        return when {
            local is Success && remote is InProgress -> mergeRequests(local, remote)
            local is Success && remote is Success -> mergeRequests(local, remote)
            local is Success && remote is Error -> mergeRequests(local, remote)

            local is InProgress && remote is InProgress -> mergeRequests(local, remote)
            local is InProgress && remote is Success -> mergeRequests(local, remote)
            local is InProgress && remote is Error -> mergeRequests(local, remote)

            local is Error && remote is InProgress -> mergeRequests(remote)
            local is Error && remote is Success -> mergeRequests(remote)
            local is Error && remote is Error -> mergeRequests(remote)

            else -> error("Unimplemented branch right=$local & left=$remote")
        }
    }

    private fun mergeRequests(
        cache: InProgress<T>,
        server: InProgress<T>
    ): RequestResult<T> {
        return when {
            server.data != null -> InProgress(server.data)
            else -> InProgress(cache.data)
        }
    }

    @Suppress("UNUSED_PARAMETER")
    private fun mergeRequests(
        cache: Success<T>,
        server: InProgress<T>
    ): RequestResult<T> {
        return InProgress(cache.data)
    }

    @Suppress("UNUSED_PARAMETER")
    private fun mergeRequests(
        cache: InProgress<T>,
        server: Success<T>
    ): RequestResult<T> {
        return Ignore()
    }

    private fun mergeRequests(
        cache: Success<T>,
        server: Error<T>
    ): RequestResult<T> {
        return Error(data = cache.data, error = server.error)
    }

    @Suppress("UNUSED_PARAMETER")
    private fun mergeRequests(
        cache: Success<T>,
        server: Success<T>
    ): RequestResult<T> {
        return Success(data = server.data)
    }

    private fun mergeRequests(
        cache: InProgress<T>,
        server: Error<T>
    ): RequestResult<T> {
        return Error(data = server.data ?: cache.data, error = server.error)
    }

    private fun mergeRequests(
        server: RequestResult<T>
    ): RequestResult<T> {
        return server
    }
}
