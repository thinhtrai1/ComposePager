package com.example.jetpackcompose.main

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.lerp
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.example.jetpackcompose.R
import com.google.accompanist.pager.*
import com.google.accompanist.pager.VerticalPager
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue

class PageData(
    val title: String,
    val child: List<String>
)

@OptIn(ExperimentalPagerApi::class, ExperimentalCoilApi::class)
@Composable
fun PagerView() {
    val pages = listOf(
        PageData(
            title = "Home",
            child = listOf("Notification", "Information", "Setting")
        ),
        PageData(
            title = "Communication",
            child = listOf("News", "Blog", "Movie", "Photo", "Music")
        ),
        PageData(
            title = "Original content",
            child = listOf("Community", "Point", "Favourite", "Gatherings")
        ),
    )

    val random = (0..1000).random()
    val horizontalPagerState = rememberPagerState(1000)
    Scaffold {
        Box {
            HorizontalPager(
                count = Int.MAX_VALUE,
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 32.dp),
                state = horizontalPagerState
            ) { page ->
                val p1 = page % pages.size
                val selected = remember { mutableStateOf(false) }
                val scale = animateFloatAsState(if (selected.value) 0.95f else 1f)
                val pagerState = rememberPagerState(1000)
                VerticalPager(
                    count = Int.MAX_VALUE,
                    contentPadding = PaddingValues(vertical = 40.dp),
                    itemSpacing = 20.dp,
                    modifier = Modifier.graphicsLayer {
                        val pageOffset = calculateCurrentOffsetForPage(page).absoluteValue
                        lerp(
                            start = 0.9f,
                            stop = 1f,
                            fraction = 1f - pageOffset.coerceIn(0f, 1f)
                        ).let { scale ->
                            scaleX = scale
                            scaleY = scale
                        }
                        alpha = lerp(
                            start = 0.5f,
                            stop = 1f,
                            fraction = 1f - pageOffset.coerceIn(0f, 1f)
                        )
                    },
                    state = pagerState
                ) { page2 ->
                    val p2 = page2 % pages[p1].child.size
                    HomeCard(
                        text = pages[p1].child[p2],
                        id = "$p1$p2".toInt() + random,
                        modifier = Modifier
                            .scale(scale.value)
                            .graphicsLayer {
                                selected.value = currentPageOffset != 0f
                            }
                    )
                }
            }

            HomeBottomButton(
                modifier = Modifier.align(Alignment.BottomCenter),
                horizontalPagerState,
                pages
            )
        }
    }
}

@ExperimentalCoilApi
@Composable
fun HomeCard(
    text: String,
    id: Int,
    modifier: Modifier
) {
    Box(
        modifier = modifier,
    ) {
            Image(
                painter = rememberImagePainter(
                    data = rememberRandomSampleImageUrl(
                        id = id,
                        width = 600,
                        height = 1200
                    ),
                ),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(15.dp))
            )
        Card(
            shape = RoundedCornerShape(16.dp),
            backgroundColor = Color.White,
            modifier = Modifier
                .align(Alignment.Center)
                .width(120.dp)
                .height(120.dp)
        ) {
            Text(
                text = text,
                fontWeight = FontWeight.ExtraBold,
                style = TextStyle(fontSize = 16.sp),
                modifier = Modifier
                    .wrapContentSize(),
            )
        }
    }
}

@ExperimentalPagerApi
@Composable
fun HomeBottomButton(
    modifier: Modifier = Modifier,
    pagerState: PagerState,
    pages: List<PageData>
) {
    val coroutineScope = rememberCoroutineScope()
    Box(
        modifier = modifier
            .padding(start = 48.dp, end = 48.dp, bottom = 80.dp)
            .clip(RoundedCornerShape(24.dp))
            .background(Color.White.copy(alpha = 0.95f))
            .height(48.dp)
            .fillMaxWidth()
    ) {
        IconButton(
            onClick = {
                coroutineScope.launch {
                    pagerState.animateScrollToPage(pagerState.currentPage - 1)
                }
            },
            modifier = Modifier.align(Alignment.CenterStart)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_arrow_left),
                contentDescription = null,
            )
        }

        Text(
            text = pages[pagerState.currentPage % pages.size].title,
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.Bold,
            style = TextStyle(
                color = Color(0xFF4b4b4b),
                fontSize = 16.sp
            ),
            modifier = Modifier.align(Alignment.Center)
        )

        IconButton(
            onClick = {
                coroutineScope.launch {
                    pagerState.animateScrollToPage(pagerState.currentPage + 1)
                }
            },
            modifier = Modifier.align(Alignment.CenterEnd)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_arrow_right),
                contentDescription = null,
            )
        }
    }
}

@Composable
fun rememberRandomSampleImageUrl(
    id: Int,
    width: Int,
    height: Int,
): String = remember { "https://picsum.photos/seed/$id/$width/$height" }