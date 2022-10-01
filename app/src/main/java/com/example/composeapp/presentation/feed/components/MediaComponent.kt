@file:Suppress("DEPRECATION")

package com.example.composeapp.presentation.feed.components

import android.net.Uri
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import coil.compose.AsyncImage
import com.example.composeapp.domain.model.Media
import com.example.composeapp.ui.theme.*
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.ui.StyledPlayerView
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import kotlin.math.roundToInt

@Composable
private fun PlayButton(onClick : ()->Unit) {
    Icon(
        imageVector = Icons.Filled.PlayArrow,
        tint = Color.White,
        modifier = Modifier
            .background(color = Blue, shape = CircleShape)
            .padding(5.dp)
            .size(playButtonSize)
            .clickable { onClick() },
        contentDescription = "play_icon"
    )
}

@Composable
private fun VideoThumbnail(
    thumbnail: String,
    modifier: Modifier = Modifier,
    onPlayButtonClick : ()-> Unit
) {
    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        AsyncImage(
            model = thumbnail,
            contentDescription = "Post Image",
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            contentScale = ContentScale.Crop
        )
        PlayButton {
            onPlayButtonClick()
        }
    }
}

@Composable
private fun VideoPlay(
    exoPlayer: ExoPlayer,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    Box(modifier = modifier){
        AndroidView(modifier = Modifier.fillMaxSize(), factory = {
            StyledPlayerView(context).apply {
                player = exoPlayer
//                resizeMode = AspectRatioFrameLayout.RESIZE_MODE_ZOOM
                controllerAutoShow = false
            }
        })
    }
}

@Composable
private fun PostVideo(
    exoPlayer: ExoPlayer,
    video: Media.Video,
    isPlaying:Boolean,
    onPlayButtonClick: () -> Unit
) {
    val modifier = Modifier
        .fillMaxWidth()
        .layout { measurable, constraints ->
            val videoHeight = (constraints.maxWidth / video.aspectRatio).roundToInt()

            val newConstraints = constraints.copy(maxHeight = videoHeight)

            val placeable = measurable.measure(newConstraints)
            layout(constraints.maxWidth, videoHeight) {
                placeable.place(0, 0)
            }
        }

        if(isPlaying){
            VideoPlay(exoPlayer = exoPlayer, modifier = modifier)
        }else{
            VideoThumbnail(
                thumbnail = video.thumbnailUrl,
                onPlayButtonClick = onPlayButtonClick
            )
        }
}

@Composable
private fun ShowPhotos(
    photo: Media.Photo
) {
    if(photo.urls.size == 1){
        // if only one photo
        AsyncImage(
            model = photo.urls.first(),
            contentDescription = "Post Image",
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            contentScale = ContentScale.Crop
        )
    }else{
        val lazyListState = rememberLazyListState()
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(standardPadding),
            state = lazyListState,
            contentPadding = PaddingValues(horizontal = standardPadding)
        ){
            items(photo.urls){ url ->
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(defaultCornerRadius))
                        .height(maxPhotoHeight)
                        .wrapContentWidth()
                ){
                    AsyncImage(
                        model = url,
                        contentDescription = "Post Image",
                        modifier = Modifier
                            .fillMaxHeight()
                            .wrapContentWidth(),
                        contentScale = ContentScale.FillBounds
                    )
                }
            }
        }
    }
}

@Composable
fun PostMedia(
    media: Media,
    isVideoPlaying:Boolean,
    exoPlayer: ExoPlayer?,
    onPlayButtonClick: () -> Unit
) {
    when(media){
        is Media.Video -> {
            if(exoPlayer!=null) {
                PostVideo(
                    exoPlayer = exoPlayer,
                    video = media,
                    isPlaying = isVideoPlaying,
                    onPlayButtonClick = onPlayButtonClick
                )
            }else{
                Log.d("PostMedia", "ExoPlayer must not be empty")
            }
        }
        is Media.Audio -> {

        }
        is Media.Photo -> {
            ShowPhotos(photo = media)
        }
    }
}

/**
 * Method to initialize data source for current playing item
 */
@Composable
fun InitCurrentlyPlayingItem(exoPlayer: ExoPlayer, video: Media.Video?) {
    val context = LocalContext.current
    val dataSourceFactory = remember {
        DefaultDataSourceFactory(
            context,
            Util.getUserAgent(context, context.packageName)
        )
    }

    LaunchedEffect(video) {
        exoPlayer.apply {
            if(video!=null){
                val source = ProgressiveMediaSource.Factory(dataSourceFactory)
                    .createMediaSource(MediaItem.fromUri(Uri.parse(video.url)))

                setMediaSource(source)
                prepare()
                playWhenReady = true
            }else{
                stop()
            }
        }
    }

    val lifecycleOwner by rememberUpdatedState(LocalLifecycleOwner.current)
    DisposableEffect(lifecycleOwner) {
        val lifecycle = lifecycleOwner.lifecycle
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_PAUSE -> {
                    exoPlayer.playWhenReady = false
                }
                Lifecycle.Event.ON_RESUME -> {
                    exoPlayer.playWhenReady = true
                }
                Lifecycle.Event.ON_DESTROY -> {
                    exoPlayer.run {
                        stop()
                        release()
                    }
                }
                else -> {}
            }
        }
        lifecycle.addObserver(observer)
        onDispose {
            lifecycle.removeObserver(observer)
        }
    }
}