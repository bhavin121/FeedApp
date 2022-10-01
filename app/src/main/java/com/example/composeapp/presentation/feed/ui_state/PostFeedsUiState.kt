package com.example.composeapp.presentation.feed.ui_state

import com.example.composeapp.domain.model.PostWithUser

data class PostFeedsUiState(
    val posts:List<PostWithUser> = emptyList(),
    val currentPlayingVideo : PostWithUser? = null,
    val isLoading : Boolean = false
)