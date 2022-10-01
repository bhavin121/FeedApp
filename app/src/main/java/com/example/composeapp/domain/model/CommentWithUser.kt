package com.example.composeapp.domain.model

data class CommentWithUser(
    val comment: Comment,
    val user: User,
    val isLiked:Boolean
)
