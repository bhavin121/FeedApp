package com.example.composeapp.domain.use_case

import com.example.composeapp.domain.model.PostWithUser
import com.example.composeapp.domain.model.PostWithUsersPage
import com.example.composeapp.domain.repository.LikesRepository
import com.example.composeapp.domain.repository.PostRepository
import com.example.composeapp.domain.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GetRecentPostsWithUserUseCase(
    private val userRepository: UserRepository,
    private val postRepository: PostRepository,
    private val likesRepository: LikesRepository,
    private val getPassedTime: GetPassedTimeUseCase
) {
    suspend operator fun invoke(pageId:Int):PostWithUsersPage = withContext(Dispatchers.Default){
        val postsWithUser = mutableListOf<PostWithUser>()
        val postList = postRepository.getRecentPosts(pageId)
        postList.posts.forEach { post ->
            val user = userRepository.getUser(post.userId)
            if(user!=null){
                val likes = likesRepository.isUserLikesPost(post.userId, post.id)
                val postWithUser = PostWithUser(
                    post = post,
                    user = user,
                    isLiked = likes,
                    timePassed = getPassedTime(post.timeStamp)
                )

                postsWithUser.add(postWithUser)
            }
        }

        PostWithUsersPage(postsWithUser = postsWithUser, nextPage = postList.nextPage)
    }
}