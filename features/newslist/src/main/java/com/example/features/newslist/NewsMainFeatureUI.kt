package com.example.features.newslist

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage
import coil3.compose.AsyncImagePainter
import com.example.uikit.NewsTheme
import com.example.uikit.RedTransparent

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
                        RedTransparent
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
        item {
            Spacer(
                Modifier
                    .height(8.dp)
                    .fillMaxWidth()
            )
        }
        items(articleList) { article ->
            key(article.id) {
                ArticleItem(article)
            }
        }
    }
}

@Composable
internal fun ArticleItem(article: ArticleUI) {
    Row(Modifier.padding(8.dp)) {
        article.imageUrl?.let {
            var isImageVisible by remember { mutableStateOf(true) }
            Spacer(Modifier.size(4.dp))

            if (isImageVisible) {
                AsyncImage(
                    modifier = Modifier
                        .size(150.dp),
                    model = it,
                    contentScale = ContentScale.Crop,
                    contentDescription = null,
                    onState = { state: AsyncImagePainter.State ->
                        if (state is AsyncImagePainter.State.Error) isImageVisible = false
                    }
                )
            }
        }

        Column(Modifier.padding(start = 8.dp)) {
            Text(
                modifier = Modifier.padding(vertical = 2.dp),
                text = article.title ?: "NO TITLE",
                style = NewsTheme.typography.headlineSmall,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = article.description ?: "show more...",
                style = NewsTheme.typography.bodyMedium,
                maxLines = 3
            )
            Spacer(Modifier.size(8.dp))
        }
    }
}

private class StatePreviewProvider : PreviewParameterProvider<State> {
    override val values: Sequence<State>
        get() = sequenceOf(
            State.Loading(articleList),
            State.Error(articleList)
        )
}

private val articleList = listOf(
    ArticleUI(
        id = 1,
        title = "First news title",
        description = "first news long description",
        imageUrl = "https://upload.wikimedia.org/wikipedia/commons/8/8f/Example_image.svg",
        url = ""
    ),
    ArticleUI(
        id = 2,
        title = "Second news title",
        description = "second news long description",
        imageUrl = null,
        url = ""
    )
)
