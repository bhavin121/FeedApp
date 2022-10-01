package com.example.composeapp.domain.repository

interface LikesRepository {
    suspend fun isUserLikesPost(userId:Int, postId:Int) : Boolean
    suspend fun isUserLikesComment(userId: Int, commentId:Int) : Boolean
}