package com.example.composeapp.presentation.util

import androidx.compose.foundation.lazy.LazyListState
import com.example.composeapp.domain.model.Media
import com.example.composeapp.domain.model.PostWithUser
import kotlin.math.abs

/**
 * Finds which post's video should be played
 *
 * @param listState - LazyListState
 * @param items - List of all PostWithUsers
 * @return PostWithUser
 */
fun determineCurrentlyPlayingItem(listState: LazyListState, items: List<PostWithUser>): PostWithUser? {
    val layoutInfo = listState.layoutInfo
    val visibleTweets = layoutInfo.visibleItemsInfo.map { items[it.index] }
    val tweetsWithVideo = visibleTweets.filter { it.post.media is Media.Video}
    return if (tweetsWithVideo.size == 1) {
        tweetsWithVideo.first()
    } else {
        val midPoint = (layoutInfo.viewportStartOffset + layoutInfo.viewportEndOffset) / 2
        val itemsFromCenter =
            layoutInfo.visibleItemsInfo.sortedBy { abs((it.offset + it.size / 2) - midPoint) }
        itemsFromCenter.map { items[it.index] }.firstOrNull { it.post.media is Media.Video }
    }
}