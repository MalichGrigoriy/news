package com.example.features.newslist.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.features.newslist.logic.ArticleUI
import com.example.features.newslist.logic.NewsMainViewModel
import com.example.features.newslist.logic.State
import com.example.uikit.NewsTheme
import com.example.uikit.RedTransparent

@Composable
fun NewsMainStateScreen(modifier: Modifier = Modifier) {
    val state by viewModel<NewsMainViewModel>().state.collectAsState()
    NewsMainStateScreen(modifier, state)
}

@Composable
internal fun NewsMainStateScreen(
    modifier: Modifier = Modifier,
    state: State
) {
    when (state) {
        is State.Success -> DrawSuccessState(modifier, state)
        is State.Error -> DrawErrorState(modifier, state)
        is State.Loading -> DrawLoadingState(modifier, state)
        is State.None -> DrawEmptyState(modifier)
    }
}

@Composable
@Preview(showBackground = true)
fun DrawSuccessStatePreview(
    @PreviewParameter(ArticleListPreviewProviderState::class) articles: List<ArticleUI>
) = DrawSuccessState(state = State.Success(articles = articles))

@Composable
private fun DrawSuccessState(
    modifier: Modifier = Modifier,
    state: State.Success
) {
    DrawArticlesState(modifier, state.articles)
}

@Composable
@Preview(showBackground = true)
fun DrawErrorStatePreview(
    @PreviewParameter(ArticleListPreviewProviderState::class) articles: List<ArticleUI>
) = DrawErrorState(state = State.Error(articles = articles))

@Composable
internal fun DrawErrorState(
    modifier: Modifier = Modifier,
    state: State.Error
) {
    Box(
        modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        NewsTheme.colorScheme.error,
                        RedTransparent,
                    )
                )
            )
            .padding(8.dp),
        contentAlignment = Alignment.TopCenter
    ) {
        Text(
            text = "Error",
            color = NewsTheme.colorScheme.onError,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(8.dp)
        )

        state.articles?.let { DrawArticlesState(modifier, it) }
    }
}

@Composable
@Preview(showBackground = true)
fun DrawLoadingStatePreview(
    @PreviewParameter(ArticleListPreviewProviderState::class) articles: List<ArticleUI>
) = DrawLoadingState(state = State.Loading(articles = articles))

@Composable
internal fun DrawLoadingState(
    modifier: Modifier = Modifier,
    state: State.Loading
) {
    Box(
        modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Loading",
            color = NewsTheme.colorScheme.onError,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(8.dp)
        )

        CircularProgressIndicator(Modifier.align(Alignment.Center))

        state.articles?.let { DrawArticlesState(modifier, it) }
    }
}

@Composable
@Preview
fun DrawEmptyState(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Text(text = "No news")
    }
}
