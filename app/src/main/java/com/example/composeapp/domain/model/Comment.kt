package com.example.composeapp.domain.model

data class Comment(
    val id:Int,
    val postId:Int,
    val userId:Int,
    val body:String,
    val commentType:String = "Public"
)
