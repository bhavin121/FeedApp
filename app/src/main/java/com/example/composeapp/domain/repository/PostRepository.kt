package com.example.composeapp.domain.repository

import com.example.composeapp.domain.model.PostsPage

interface PostRepository {
    suspend fun getRecentPosts(pageId:Int) : PostsPage
}