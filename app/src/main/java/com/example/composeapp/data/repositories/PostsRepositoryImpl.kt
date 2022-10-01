package com.example.composeapp.data.repositories

import com.example.composeapp.data.data_source.PostDataSource
import com.example.composeapp.domain.model.PostsPage
import com.example.composeapp.domain.repository.PostRepository

class PostsRepositoryImpl(
    private val postDataSource: PostDataSource
) : PostRepository {
    override suspend fun getRecentPosts(pageId: Int): PostsPage {
        return postDataSource.getPosts(pageId)
    }
}