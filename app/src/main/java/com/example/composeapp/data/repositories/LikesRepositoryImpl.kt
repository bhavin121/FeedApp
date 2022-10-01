package com.example.composeapp.data.repositories

import com.example.composeapp.data.data_source.LikesDataSource
import com.example.composeapp.domain.repository.LikesRepository

class LikesRepositoryImpl(
    private val likesDataSource: LikesDataSource
) : LikesRepository {
    override suspend fun isUserLikesPost(userId: Int, postId: Int): Boolean {
        return likesDataSource.isUserLikesPost(userId, postId)
    }

    override suspend fun isUserLikesComment(userId: Int, commentId: Int): Boolean {
        return likesDataSource.isUserLikesComment(userId, commentId)
    }
}