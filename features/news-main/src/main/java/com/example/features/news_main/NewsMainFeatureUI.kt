package com.example.features.news_main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.news_uikit.NewsTheme

@Composable
fun NewsMainScreen() {
    val newsViewModel: NewsMainViewModel = viewModel()
    val state by newsViewModel.state.collectAsState()
    NewsMainScreen(state)
}

@Composable
@Preview
internal fun NewsMainScreen(
    @PreviewParameter(StatePreviewProvider::class, limit = 2) state: State
) {
    state.articles?.let { DrawArticles(it) }
    Column {
        state.let { state: State ->
            (state as? State.Error)?.let { DrawError() }
            (state as? State.Loading)?.let { DrawLoading() }
        }
    }
}

@Composable
@Preview
internal fun DrawError() {
    Box(
        Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        NewsTheme.colorScheme.error,
                        Color(0x00FF0000)
                    )
                )
            )
            .padding(2.dp),
        contentAlignment = Alignment.TopCenter
    ) {
        Text(
            text = "Error",
            color = NewsTheme.colorScheme.onError,
        )
    }
}

@Composable
@Preview
internal fun DrawLoading() {
        LinearProgressIndicator(Modifier.fillMaxWidth())
}

@Composable
@Preview
fun DrawEmpty() {
    Box(contentAlignment = Alignment.Center) {
        Text(text = "No news")
    }
}

@Composable
private fun DrawArticles(articleList: List<ArticleUI>) {
    LazyColumn(
        Modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        item { Spacer(Modifier.height(8.dp).fillMaxWidth()) }
        items(articleList) { article ->
            key(article.id) {
                ArticleItem(article)
            }
        }
    }
}

@Composable
internal fun ArticleItem(article: ArticleUI) {
    Column(Modifier.padding(8.dp)) {
        Text(
            text = article.title ?: "NO TITLE",
            style = NewsTheme.typography.headlineMedium,
            maxLines = 1
        )
        Spacer(Modifier.size(4.dp))
        Text(
            text = article.description ?: "show more...",
            style = NewsTheme.typography.bodyMedium,
            maxLines = 3
        )
    }
}

private class StatePreviewProvider : PreviewParameterProvider<State> {
    override val values: Sequence<State>
        get() = sequenceOf(
            State.Loading(articleList),
            State.Error(articleList)
        )
}

private class ArticleListPreviewProvider : PreviewParameterProvider<List<ArticleUI>> {
    override val values: Sequence<List<ArticleUI>>
        get() = sequenceOf(articleList)
}

private val articleList = listOf(
    ArticleUI(
        id = 1,
        title = "First news title",
        description = "first news long description",
        imageUrl = null,
        url = ""
    ), ArticleUI(
        id = 2,
        title = "Second news title",
        description = "second news long description",
        imageUrl = null,
        url = ""
    )
)
