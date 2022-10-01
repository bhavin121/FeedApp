package com.example.composeapp.domain.repository

import com.example.composeapp.domain.model.CommentsPage

interface CommentsRepository {
    suspend fun getCommentsForPost(postId:Int, pageId:Int) : CommentsPage
}