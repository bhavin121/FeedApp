package com.example.composeapp.domain.use_case

import com.example.composeapp.domain.model.CommentWithUser
import com.example.composeapp.domain.model.CommentsWithUserPage
import com.example.composeapp.domain.repository.CommentsRepository
import com.example.composeapp.domain.repository.LikesRepository
import com.example.composeapp.domain.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GetPostCommentsWithUserUseCase(
    private val userRepository: UserRepository,
    private val commentsRepository: CommentsRepository,
    private val likesRepository: LikesRepository
) {
    suspend operator fun invoke(postId:Int, pageId:Int): CommentsWithUserPage = withContext(Dispatchers.Default){
        val commentWithUsers = mutableListOf<CommentWithUser>()
        val commentsPage = commentsRepository.getCommentsForPost(postId, pageId)
        commentsPage.comments.forEach { comment ->
            val user = userRepository.getUser(comment.userId)
            if(user!=null){
                val likes = likesRepository.isUserLikesPost(comment.userId, comment.id)
                val commentWithUser = CommentWithUser(
                    comment = comment,
                    user = user,
                    isLiked = likes
                )

                commentWithUsers.add(commentWithUser)
            }
        }

        CommentsWithUserPage(commentsWithUser = commentWithUsers, nextPage = commentsPage.nextPage)
    }
}