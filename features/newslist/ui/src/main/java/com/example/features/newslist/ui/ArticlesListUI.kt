package com.example.features.newslist.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.compose.AsyncImagePainter
import com.example.features.domain.newslist.ArticleUI
import com.example.uikit.NewsTheme

@Composable
@Preview(showBackground = true)
fun DrawArticlesStatePreview(
    @PreviewParameter(ArticleListPreviewProviderState::class) articles: List<ArticleUI>
) = DrawArticlesState(articleList = articles)

@Composable
internal fun DrawArticlesState(
    modifier: Modifier = Modifier,
    articleList: List<ArticleUI>
) {
    LazyColumn(
        modifier
            .padding(8.dp)
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        items(articleList) { article ->
            key(article.id) {
                ArticleItemState(Modifier, article)
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun ArticleItemStatePreview(
    @PreviewParameter(ArticleListPreviewProviderState::class) articles: List<ArticleUI>
) = ArticleItemState(article = articles.first())

@Composable
internal fun ArticleItemState(
    modifier: Modifier = Modifier,
    article: ArticleUI
) {
    Row(modifier.padding(8.dp)) {
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

internal class ArticleListPreviewProviderState : PreviewParameterProvider<List<ArticleUI>> {
    override val values: Sequence<List<ArticleUI>>
        get() = sequenceOf(
            listOf(
                ArticleUI(
                    id = 1,
                    title = "First news title",
                    description = "first news long description",
                    imageUrl = null,
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
        )
}
