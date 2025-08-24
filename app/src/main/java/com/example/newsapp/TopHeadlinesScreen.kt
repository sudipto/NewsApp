package com.example.newsapp

import android.text.format.DateUtils
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.rememberAsyncImagePainter
import java.time.OffsetDateTime
import java.time.ZoneId

@Composable
fun TopHeadlinesScreen(modifier: Modifier = Modifier) {
    val viewModel: TopHeadlinesViewModel = viewModel()
    val viewState by viewModel.topHeadlinesState

    Box(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        when {
            viewState.loading -> {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            viewState.error != null -> {
                Text("An error has occurred!")
            }

            else -> {
                ArticlesScreen(viewState.articles)
            }
        }
    }
}

@Composable
fun ArticlesScreen(articles: List<Article>) {
    LazyColumn(
        modifier = Modifier.fillMaxWidth().padding(top = 16.dp)
    ) {
        items(articles) {
            ArticleItem(it)
        }
    }
}

@Composable
fun ArticleItem(article: Article) {
    Row(
        modifier = Modifier.fillMaxWidth()
//            .border(width = 2.dp, color = Color.Gray)
            .padding(8.dp)
    ) {
        Image(
            painter = rememberAsyncImagePainter(article.urlToImage),
            contentDescription = null,
            modifier = Modifier
                .size(100.dp)
                .aspectRatio(1f)
                .clip(RoundedCornerShape(8.dp)),
            contentScale = ContentScale.Crop
        )

        Column(
            Modifier.padding(horizontal = 8.dp)
        ) {
            Text(
                article.title,
                style = TextStyle(
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
            )
            Spacer(Modifier.padding(2.dp))
            Text(
                getRelativeTime(article.publishedAt),
                style = TextStyle(
                    fontSize = 12.sp,
                    color = Color.Gray,
                    fontWeight = FontWeight.SemiBold
                )
            )
        }
    }
}

fun getRelativeTime(isoString: String): String {
    // Parse the ISO string
    val time = OffsetDateTime.parse(isoString)
        .atZoneSameInstant(ZoneId.systemDefault())
        .toInstant()
        .toEpochMilli()

    // Get "1 hour ago", "Yesterday", etc.
    return DateUtils.getRelativeTimeSpanString(
        time,
        System.currentTimeMillis(),
        DateUtils.MINUTE_IN_MILLIS
    ).toString()
}

@Preview(showBackground = true)
@Composable
fun PreviewArticlesScreen(
    @PreviewParameter(SampleTopHeadlinesProvider ::class) articles: List<Article>
) {
    ArticlesScreen(articles.map { it.copy(
        urlToImage = R.drawable.sample_image.toString()
    ) })
}

//@Preview(showBackground = true)
//@Composable
//fun PreviewArticleItem() {
//    ArticleItem(Article(
//        "Trump's D.C. 'crisis' enters 2nd week with more soldiers — and no exit strategy - NPR",
//        "Leaders in Washington, D.C., say they're striving to maintain calm as growing numbers of National Guard soldiers deploy to the city. President Trump hasn't said how he wants this \"crisis\" to end.",
//        "https://www.npr.org/2025/08/18/nx-s1-5505419/trump-washington-dc-crisis-national-guard",
//        R.drawable.sample_image.toString(),
//        "2025-08-18T09:08:29Z",
//        "WASHINGTON As the U.S. capital braces for a second week with soldiers and masked federal agents conducting \"roving patrols\" on the city streets, President Trump says he knows some Americans fear he's… [+6954 chars]"
//    ))
//}