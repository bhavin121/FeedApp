package com.example.composeapp.domain.model

data class PostsPage(
    val posts:List<Post>,
    val nextPage:Int
)