package com.example.composeapp.domain.model

data class PostWithUsersPage(
    val postsWithUser: List<PostWithUser>,
    val nextPage:Int
)