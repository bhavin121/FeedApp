package com.example.composeapp.data.repositories

import com.example.composeapp.data.data_source.CommentDataSource
import com.example.composeapp.domain.model.CommentsPage
import com.example.composeapp.domain.repository.CommentsRepository

class CommentsRepositoryImpl(
    private val commentDataSource: CommentDataSource
) : CommentsRepository {
    override suspend fun getCommentsForPost(postId: Int, pageId: Int): CommentsPage {
        return commentDataSource.getComments(postId, pageId)
    }
}