package com.example.composeapp.presentation.feed.ui_state

import com.example.composeapp.domain.model.CommentWithUser
import com.example.composeapp.domain.model.PostWithUser

data class PostCommentsUiState(
    val post:PostWithUser? = null,
    val comments:List<CommentWithUser> = emptyList(),
    val isLoading:Boolean = true
)