package com.example.composeapp.presentation.feed.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.composeapp.domain.model.Media
import com.example.composeapp.presentation.UiEvent
import com.example.composeapp.presentation.feed.PostFeedViewModel
import com.example.composeapp.presentation.util.determineCurrentlyPlayingItem
import com.example.composeapp.ui.theme.Blue
import com.example.composeapp.ui.theme.LightGray
import com.example.composeapp.ui.theme.defaultSpace
import com.example.composeapp.ui.theme.standardPadding
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.pagerTabIndicatorOffset
import com.google.accompanist.pager.rememberPagerState
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.Player
import kotlinx.coroutines.launch

@OptIn(ExperimentalPagerApi::class)
@Composable
fun HomeScreen(
    navController: NavController,
    viewModel:PostFeedViewModel
) {
    val pagerState = rememberPagerState(pageCount = 3)
    val scope = rememberCoroutineScope()
    val tabsList = listOf("charcha", "bazaar", "profile")

    Column {
        TabRow(
            selectedTabIndex = pagerState.currentPage,
            backgroundColor = Color.White,
            contentColor = Blue,
            indicator = {
                TabRowDefaults.Indicator(
                    modifier = Modifier
                        .pagerTabIndicatorOffset(pagerState, it)
                        .padding(horizontal = defaultSpace),
                    height = 2.dp,
                    color = Blue
                )
            }
        ) {
            tabsList.forEachIndexed { index, name ->
                Tab(
                    text = {
                        Text(
                            text = name,
                            color = if (pagerState.currentPage == index) Blue else Color.DarkGray
                        )
                    },
                    selected = pagerState.currentPage == index,
                    onClick = {
                        scope.launch {
                            pagerState.animateScrollToPage(index)
                        }
                    }
                )
            }
        }

        val listState = rememberLazyListState()
        HorizontalPager(state = pagerState) { page ->
            when(page){
                0 -> FeedScreen(listState = listState, navController = navController, viewModel = viewModel)
                1,2 -> BlankScreen(tabsList[page])
            }
        }
    }
}

/**
 * Component to show a blank screen in case of bazaar and profile tab
 */
@Composable
private fun BlankScreen(text: String) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        Text(text = text)
    }
}

/**
 * Component to show feed posts in tab charcha
 */
@Composable
private fun FeedScreen(
    listState: LazyListState,
    navController: NavController,
    viewModel: PostFeedViewModel
) {
    val data = viewModel.postUiState.observeAsState()
    val context = LocalContext.current

    val exoPlayer = remember {
        ExoPlayer.Builder(context).build().apply {
            repeatMode = Player.REPEAT_MODE_ALL
            pauseAtEndOfMediaItems = true
        }
    }

    val items = data.value?.posts?: listOf()

    val currentPlayingVideo = determineCurrentlyPlayingItem(listState, items)
    if(currentPlayingVideo != data.value?.currentPlayingVideo) {
        viewModel.onEvent(UiEvent.ChangeCurrentVideo(currentPlayingVideo))
    }

    val media = data.value?.currentPlayingVideo?.post?.media
    if(media is Media.Video) {
        InitCurrentlyPlayingItem(exoPlayer = exoPlayer, video = media)
    }
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(defaultSpace),
        state = listState
    ) {
        itemsIndexed(items){ idx, post ->

            Column (horizontalAlignment = Alignment.CenterHorizontally) {
                Post(
                    postWithUser = post,
                    exoPlayer = exoPlayer,
                    isVideoPlaying = data.value?.currentPlayingVideo == post,
                    onPlayVideoClick = {
                        viewModel.onEvent(UiEvent.ChangeCurrentVideo(post))
                    },
                    navController = navController,
                    viewModel = viewModel
                )
                Divider(
                    color = LightGray,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(2.dp)
                )
                if(data.value?.isLoading != true && items.size-1 == idx){
                    viewModel.onEvent(UiEvent.LoadMorePost)
                }else if(items.size-1 == idx){
                    Spacer(modifier = Modifier.height(standardPadding))
                    CircularProgressIndicator(color = Blue)
                    Spacer(modifier = Modifier.height(standardPadding))
                }
            }
        }
    }
}