package com.example.jetpackcompose.main

import android.view.MotionEvent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.util.lerp
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.google.accompanist.pager.*
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue

@ExperimentalComposeUiApi
@ExperimentalUnitApi
@ExperimentalPagerApi
@Composable
fun PagerView() {
    val features = listOf(
        Feature(listOf("Home", "Notification", "Information", "Setting")),
        Feature(listOf("News", "Blog", "Movie", "Photo", "Music")),
        Feature(listOf("Community", "Favourite", "Gatherings", "Happy")),
    )
    val coroutineScope = rememberCoroutineScope()
    val pagerState = rememberPagerState(5)

    fun PagerScope.graphics(page: Int) = Modifier
        .graphicsLayer {
            val pageOffset = calculateCurrentOffsetForPage(page).absoluteValue
            lerp(
                start = 0.85f,
                stop = 1f,
                fraction = 1f - pageOffset.coerceIn(0f, 1f)
            ).also { scale ->
                scaleX = scale
                scaleY = scale
            }
            alpha = lerp(
                start = 0.5f,
                stop = 1f,
                fraction = 1f - pageOffset.coerceIn(0f, 1f)
            )
        }
        .pointerInteropFilter {
            when (it.action) {
                MotionEvent.ACTION_DOWN -> {}
                MotionEvent.ACTION_UP -> {}
            }
            return@pointerInteropFilter false
        }
        .fillMaxSize()

    HorizontalPager(
        count = 10,
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 32.dp),
        state = pagerState
    ) { page ->
        VerticalPager(
            count = 100,
            modifier = graphics(page),
            contentPadding = PaddingValues(vertical = 64.dp),
            state = PagerState(50)
        ) { page2 ->
            Card(
                modifier = graphics(page2),
                shape = RoundedCornerShape(16.dp),
//                onClick = {
//                    coroutineScope.launch {
//                        if (page < 9) {
//                            pagerState.animateScrollToPage(page = page + 1)
//                        }
//                        if (page2 < 9) {
//                            verticalPagerState[page].animateScrollToPage(page2 + 1)
//                            if (page < 8)
//                            verticalPagerState[page + 1].animateScrollToPage(page2 + 1)
//                            if (page < 7) {
//                                verticalPagerState[page + 2].animateScrollToPage(page2 + 1)
//                            }
//                        }
//                    }
//                }
            ) {
                Box {
                    val seed = rangeForRandom.random()
                    Image(
                        painter = rememberImagePainter(
                            data = rememberRandomSampleImageUrl(seed = seed, width = 600, height = 1200),
                        ),
                        contentScale = ContentScale.Crop,
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.LightGray),
                    )
                    ProfilePicture(
                        seed,
                        Modifier
                            .align(Alignment.BottomCenter)
                            .padding(16.dp)
                    )
                    Card(
                        shape = RoundedCornerShape(8.dp),
                        backgroundColor = Color.White,
                        modifier = Modifier
                            .align(Alignment.TopCenter)
                            .wrapContentWidth()
                            .padding(top = 16.dp)
                            .height(48.dp)
                    ) {
                        Text(
                            text = features[page % features.size].list[page2 % features[page % features.size].list.size],
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = TextUnit(16f, TextUnitType.Sp),
                            modifier = Modifier
                                .padding(horizontal = 32.dp)
                                .wrapContentSize(),
                        )
                    }
                }
            }
        }
    }
}

class Feature(val list: List<String>)

@Composable
private fun ProfilePicture(seed: Int, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        shape = CircleShape,
        border = BorderStroke(4.dp, MaterialTheme.colors.surface)
    ) {
        Image(
            painter = rememberImagePainter(rememberRandomSampleImageUrl(seed)),
            contentDescription = null,
            modifier = Modifier.size(72.dp),
        )
    }
}

private val rangeForRandom = (0..100000)

@Composable
fun rememberRandomSampleImageUrl(
    seed: Int,
    width: Int = 200,
    height: Int = 200,
): String = remember { "https://picsum.photos/seed/$seed/$width/$height" }