package com.example.composeapp.domain.model

data class CommentsWithUserPage(
    val commentsWithUser: List<CommentWithUser>,
    val nextPage:Int
)
