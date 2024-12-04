package com.example.features.newslist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.RequestResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject
import javax.inject.Provider

@HiltViewModel
internal class NewsMainViewModel @Inject constructor(
    getAllArticlesUseCase: Provider<GetAllArticlesUseCase>
) : ViewModel() {

    val state: StateFlow<State> = getAllArticlesUseCase.get().invoke(query = "stalker 2")
        .map { it.toSate() }
        .stateIn(viewModelScope, SharingStarted.Lazily, State.None)

    fun forceUpdate() {
//        getAllArticlesUseCase.fetchLatest()
    }
}

private fun RequestResult<List<ArticleUI>>.toSate(): State {
    return when (this) {
        is RequestResult.Error -> State.Error(this.data)
        is RequestResult.InProgress -> State.Loading(this.data)
        is RequestResult.Success -> State.Success(this.data)
        is RequestResult.Ignore -> error(" ")
    }
}

internal sealed class State(open val articles: List<ArticleUI>?) {

    data object None : State(null)
    class Loading(articles: List<ArticleUI>? = null) : State(articles)
    class Error(articles: List<ArticleUI>? = null) : State(articles)
    class Success(override val articles: List<ArticleUI>) : State(articles)
}
