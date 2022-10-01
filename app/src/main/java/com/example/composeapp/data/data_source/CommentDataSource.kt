package com.example.composeapp.data.data_source

import com.example.composeapp.domain.model.Comment
import com.example.composeapp.domain.model.CommentsPage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CommentDataSource @Inject constructor(){
    private val commentsData = listOf(
        "What is urvar?",
        "Just landed on urvar",
        "Agriculture or farming is the practice of cultivating plants and livestock"
    )

    suspend fun getComments(postId:Int, pageId:Int) : CommentsPage = withContext(Dispatchers.Default){
        delay(500)
        val comments = mutableListOf<Comment>()

        repeat(20){
            val comment = Comment(
                id = (1..5000000).random(),
                userId = (0..4).random(),
                postId = postId,
                body = commentsData.random()
            )
            comments.add(comment)
        }

        CommentsPage(comments = comments, nextPage = pageId+1)
    }
}