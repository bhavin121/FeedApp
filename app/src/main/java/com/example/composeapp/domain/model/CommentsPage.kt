package com.example.composeapp.domain.model

data class CommentsPage(
    val comments:List<Comment>,
    val nextPage:Int
)
