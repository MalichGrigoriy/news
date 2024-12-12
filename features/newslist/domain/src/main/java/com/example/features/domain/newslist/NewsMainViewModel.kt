package com.example.features.domain.newslist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject
import javax.inject.Provider

@HiltViewModel
public class NewsMainViewModel @Inject constructor(
    getAllArticlesUseCase: Provider<GetAllArticlesUseCase>
) : ViewModel() {

    public val state: StateFlow<State> = getAllArticlesUseCase.get().invoke(query = "stalker 2")
        .map { it.toSate() }
        .stateIn(viewModelScope, SharingStarted.Lazily, State.None)

    public fun forceUpdate() {
//        getAllArticlesUseCase.fetchLatest()
    }
}
