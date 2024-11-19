package com.example.features.news_main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.ArticlesRepository
import com.example.data.RequestResult
import com.example.data.model.Article
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn


internal class NewsMainViewModel(
    private val getAllArticlesUseCase: GetAllArticlesUseCase
) : ViewModel() {

    val state: StateFlow<State> = getAllArticlesUseCase.invoke()
        .map { it.toSate() }
        .stateIn(viewModelScope, SharingStarted.Lazily, State.None)

    fun forceUpdate() {
        getAllArticlesUseCase.fetchLatest()
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

sealed class State {

    data object None : State()
    class Loading(val articles: List<ArticleUI>? = null) : State()
    class Error(val articles: List<ArticleUI>? = null) : State()
    class Success(val articles: List<ArticleUI>) : State()
}