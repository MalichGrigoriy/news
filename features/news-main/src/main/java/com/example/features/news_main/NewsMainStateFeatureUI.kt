package com.example.features.news_main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
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
fun NewsMainStateScreen() {
    NewsMainStateScreen(newsViewModel = viewModel())
}

@Composable
internal fun NewsMainStateScreen(newsViewModel: NewsMainViewModel = viewModel()) {
    val state by newsViewModel.state.collectAsState()

    when (val currentState = state) {
        is State.Success -> DrawArticlesState(currentState.articles)
        is State.Error -> DrawErrorState(currentState.articles)
        is State.Loading -> DrawLoadingState(currentState.articles)
        is State.None -> DrawEmptyState()
    }
}

@Composable
@Preview
internal fun DrawErrorState(
    @PreviewParameter(ArticleListPreviewProviderState::class) articleList: List<ArticleUI>?
) {
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

        if (articleList != null) {
            DrawArticlesState(articleList)
        }
    }
}

@Composable
@Preview
internal fun DrawLoadingState(
    @PreviewParameter(ArticleListPreviewProviderState::class) articleList: List<ArticleUI>?
) {
    Box(
        Modifier
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

        if (articleList != null) {
            DrawArticlesState(articleList)
        }
    }
}

@Composable
@Preview
fun DrawEmptyState() {
    Box(contentAlignment = Alignment.Center) {
        Text(text = "No news")
    }
}

@Composable
private fun DrawArticlesState(articleList: List<ArticleUI>) {
    LazyColumn(
        Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        items(articleList) { article ->
            key(article.id) {
                ArticleItemState(article)
            }
        }
    }
}

@Composable
internal fun ArticleItemState(article: ArticleUI) {
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

private class ArticlePreviewProviderState : PreviewParameterProvider<ArticleUI?> {
    override val values: Sequence<ArticleUI?>
        get() = sequenceOf(
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
}

private class ArticleListPreviewProviderState : PreviewParameterProvider<List<ArticleUI>> {
    override val values: Sequence<List<ArticleUI>>
        get() = sequenceOf(
            listOf(
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
        )
}
