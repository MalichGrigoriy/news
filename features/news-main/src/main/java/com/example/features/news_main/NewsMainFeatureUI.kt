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
import androidx.compose.material3.MaterialTheme
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
import com.example.news_common.AndroidLogCatLogger

@Composable
fun NewsMainScreen() {
    NewsMainScreen(newsViewModel = viewModel())
}

@Composable
internal fun NewsMainScreen(newsViewModel: NewsMainViewModel = viewModel()) {
    val state by newsViewModel.state.collectAsState()

    when (val currentState = state) {
        is State.Success -> DrawArticles(currentState.articles)
        is State.Error -> DrawError(currentState.articles) //drawContent(currentState)
        is State.Loading -> DrawLoading(currentState.articles) //drawContent(currentState)
        is State.None -> DrawEmpty() //drawContent(currentState)
    }
}

@Composable
@Preview
internal fun DrawError(
    @PreviewParameter(ArticleListPreviewProvider::class) articleList: List<ArticleUI>?
) {
    Box(
        Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.error,
                        Color(0x00FF0000)
                    )
                )
            ),
        contentAlignment = Alignment.TopCenter
    ) {
        Text(
            text = "Error",
            color = MaterialTheme.colorScheme.onError,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(8.dp)
        )

        if (articleList != null) {
            DrawArticles(articleList)
        }
    }
}

@Composable
@Preview
internal fun DrawLoading(
    @PreviewParameter(ArticleListPreviewProvider::class) articleList: List<ArticleUI>?
) {
    Box(
        Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        contentAlignment = Alignment.Center
    ) {

        Text(
            text = "Loading",
            color = MaterialTheme.colorScheme.onError,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(8.dp)
        )

        CircularProgressIndicator(Modifier.align(Alignment.Center))

        if (articleList != null) {
            DrawArticles(articleList)
        }
    }
}

@Composable
fun DrawEmpty() {
    Box(contentAlignment = Alignment.Center) {
        Text(text = "No news")
    }
}

@Composable
private fun DrawArticles(articleList: List<ArticleUI>) {
    LazyColumn(
        Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
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
            style = MaterialTheme.typography.headlineMedium,
            maxLines = 1
        )
        Spacer(Modifier.size(4.dp))
        Text(
            text = article.description ?: "show more...",
            style = MaterialTheme.typography.bodyMedium,
            maxLines = 3
        )
    }
}

private class ArticlePreviewProvider : PreviewParameterProvider<ArticleUI?> {
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

private class ArticleListPreviewProvider : PreviewParameterProvider<List<ArticleUI>> {
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
