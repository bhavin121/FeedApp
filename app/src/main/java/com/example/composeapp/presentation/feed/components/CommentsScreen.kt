package com.example.composeapp.presentation.feed.components

import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.composeapp.domain.model.Media
import com.example.composeapp.presentation.UiEvent
import com.example.composeapp.presentation.feed.PostFeedViewModel
import com.example.composeapp.ui.theme.Blue
import com.example.composeapp.ui.theme.LightGray
import com.example.composeapp.ui.theme.standardPadding
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.Player

@Composable
fun CommentsScreen(
    navController: NavController,
    viewModel: PostFeedViewModel
) {
    val data = viewModel.commentUiState.observeAsState()
    val context = LocalContext.current

    val exoPlayer = remember {
        ExoPlayer.Builder(context).build().apply {
            repeatMode = Player.REPEAT_MODE_ALL
            pauseAtEndOfMediaItems = true
        }
    }
    val media = data.value?.post?.post?.media
    if(media is Media.Video) {
        InitCurrentlyPlayingItem(exoPlayer = exoPlayer, video = media)
    }

    val onBackPressedDispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Comments", style = TextStyle(fontSize = 18.sp))
                },
                navigationIcon = {
                    IconButton(onClick = { onBackPressedDispatcher?.onBackPressed() }) {
                        Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "back")
                    }
                },
                backgroundColor = Color.White,
                contentColor = Color.DarkGray,
                elevation = 2.dp
            )
        }
    ) {
        LazyColumn(
            modifier = Modifier.padding(it),
            verticalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            item {
                data.value?.post?.let { post ->
                    PostOnComments(
                        postWithUser = post,
                        exoPlayer = exoPlayer,
                        isVideoPlaying = true,
                        onPlayVideoClick = {}
                    )
                }
            }
            val items = data.value?.comments?: emptyList()
            itemsIndexed(items){ idx, comment ->
                Column (horizontalAlignment = Alignment.CenterHorizontally) {
                    Comment(
                        commentWithUser = comment
                    )
                    Divider(
                        color = LightGray,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(2.dp)
                    )
                    if (data.value?.isLoading != true && items.size - 1 == idx) {
                        viewModel.onEvent(UiEvent.LoadMoreComment)
                    } else if (items.size - 1 == idx) {
                        Spacer(modifier = Modifier.height(standardPadding))
                        CircularProgressIndicator(color = Blue)
                        Spacer(modifier = Modifier.height(standardPadding))
                    }
                }
            }
        }
    }

}