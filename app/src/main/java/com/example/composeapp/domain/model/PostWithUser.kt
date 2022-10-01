package com.example.composeapp.domain.model

data class PostWithUser(
    val post: Post = Post(),
    val user: User = User(),
    val timePassed:String = "",
    val isLiked:Boolean = true
)
