package com.example.jetpackcompose.main

import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.util.lerp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.core.os.postDelayed
import coil.compose.rememberImagePainter
import com.example.jetpackcompose.InformationActivity
import com.example.jetpackcompose.R
import com.google.accompanist.pager.*
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue

@ExperimentalMaterialApi
@ExperimentalComposeUiApi
@ExperimentalUnitApi
@ExperimentalPagerApi
@Composable
@Preview
fun PagerView() {
    val features = listOf(
        Feature(
            "Home",
            listOf("Home", "Notification", "Information", "Setting")
        ),
        Feature(
            "Communication",
            listOf("News", "Blog", "Movie", "Photo", "Music")
        ),
        Feature(
            "Original content",
            listOf("Community", "Point", "Favourite", "Gatherings", "Happy")
        ),
    )
    val coroutineScope = rememberCoroutineScope()
    val horizontalPagerState = rememberPagerState(1000)
    val context = LocalContext.current

    fun PagerScope.graphics(page: Int, isVertical: Boolean) = Modifier
        .graphicsLayer {
            val pageOffset = calculateCurrentOffsetForPage(page).absoluteValue
            lerp(
                start = 0.9f,
                stop = 1f,
                fraction = 1f - pageOffset.coerceIn(0f, 1f)
            ).let { scale ->
                if (isVertical) {
                    scaleX = scale
                    scaleY = scale
                } else {
                    scaleX = scale
                    scaleY = scale
                }
            }
            alpha = lerp(
                start = 0.5f,
                stop = 1f,
                fraction = 1f - pageOffset.coerceIn(0f, 1f)
            )
            Log.e("pageOffset", "pageOffset: $pageOffset")
//            if (pageOffset == 0f) {
//                scaleX = 1f
//                scaleY = 1f
//            }
        }
        .fillMaxSize()

    HorizontalPager(
        count = Int.MAX_VALUE,
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 32.dp),
        state = horizontalPagerState
    ) { page ->
        Box(modifier = graphics(page, false)) {
            VerticalPager(
                count = Int.MAX_VALUE,
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(vertical = 64.dp),
                state = PagerState(1000)
            ) { page2 ->
                Card(
                    modifier = graphics(page2, true),
                    shape = RoundedCornerShape(16.dp),
                    onClick = {
                        context.startActivity(Intent(context, InformationActivity::class.java))
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
                    }
                ) {
                    Box {
                        val seed = rangeForRandom.random()
                        Image(
                            painter = rememberImagePainter(
                                data = rememberRandomSampleImageUrl(
                                    seed = seed,
                                    width = 600,
                                    height = 1200
                                ),
                            ),
                            contentScale = ContentScale.Crop,
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Color.LightGray),
                        )
//                        ProfilePicture(
//                            seed,
//                            Modifier
//                                .align(Alignment.BottomCenter)
//                                .padding(16.dp)
//                        )
                        Card(
                            shape = RoundedCornerShape(16.dp),
                            backgroundColor = Color.White,
                            modifier = Modifier
                                .align(Alignment.Center)
                                .width(96.dp)
                                .height(96.dp)
                        ) {
                            Text(
                                text = features[page % features.size].list[page2 % features[page % features.size].list.size],
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = TextUnit(16f, TextUnitType.Sp),
                                modifier = Modifier
                                    .wrapContentSize(),
                            )
                        }
                    }
                }
            }
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 80.dp, start = 16.dp, end = 16.dp)
                    .fillMaxWidth()
                    .height(48.dp)
                    .background(color = Color.White, shape = RoundedCornerShape(32.dp))
            ) {
                IconButton(
                    onClick = {
                        coroutineScope.launch {
                            horizontalPagerState.animateScrollToPage(page - 1)
                        }
                    },
                    modifier = Modifier
                        .indication(MutableInteractionSource(), rememberRipple())
                        .align(Alignment.CenterStart)
                        .padding(vertical = 8.dp),
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_arrow_left),
                        contentDescription = null,
                    )
                }
                IconButton(
                    onClick = {
                        coroutineScope.launch {
                            horizontalPagerState.animateScrollToPage(page + 1)
                        }
                    },
                    modifier = Modifier
                        .indication(MutableInteractionSource(), rememberRipple())
                        .align(Alignment.CenterEnd)
                        .padding(vertical = 8.dp),
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_arrow_right),
                        contentDescription = null,
                    )
                }
                Text(
                    modifier = Modifier
                        .wrapContentSize()
                        .align(Alignment.Center),
                    text = features[page % features.size].title.uppercase(),
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = TextUnit(16f, TextUnitType.Sp),
                )
            }
        }
    }
}

class Feature(val title: String, val list: List<String>)

@Composable
private fun DummyProgress(dialogState: Boolean, onDismiss: () -> Unit) {
    if (dialogState) {
        Dialog(
            onDismissRequest = onDismiss,
            properties = DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = false)
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(64.dp)
                    .background(Color.White, shape = CircleShape)
            ) {
                CircularProgressIndicator()
            }
        }
    }
}

@Composable
fun InformationView() {
    var dialogState by remember { mutableStateOf(false) }
    DummyProgress(dialogState = dialogState) { dialogState = false }
    Button(
        onClick = {
            dialogState = true
            Handler(Looper.getMainLooper()).postDelayed(500) {
                dialogState = false
            }
        }
    ) {
        Text(text = "Hello world")
    }
}

//@Composable
//private fun ProfilePicture(seed: Int, modifier: Modifier = Modifier) {
//    Card(
//        modifier = modifier,
//        shape = CircleShape,
//        border = BorderStroke(4.dp, MaterialTheme.colors.surface)
//    ) {
//        Image(
//            painter = rememberImagePainter(rememberRandomSampleImageUrl(seed)),
//            contentDescription = null,
//            modifier = Modifier.size(72.dp),
//        )
//    }
//}

private val rangeForRandom = (0..100000)

@Composable
fun rememberRandomSampleImageUrl(
    seed: Int,
    width: Int = 200,
    height: Int = 200,
): String = remember { "https://picsum.photos/seed/$seed/$width/$height" }