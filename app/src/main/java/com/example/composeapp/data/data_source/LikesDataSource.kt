package com.example.composeapp.data.data_source

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class LikesDataSource @Inject constructor(){

    suspend fun isUserLikesPost(userId: Int, postId: Int): Boolean = withContext(Dispatchers.Default){
        return@withContext ((0..1).random() == 1)
    }

    suspend fun isUserLikesComment(userId: Int, commentId: Int): Boolean = withContext(Dispatchers.Default){
        return@withContext ((0..1).random() == 1)
    }

}