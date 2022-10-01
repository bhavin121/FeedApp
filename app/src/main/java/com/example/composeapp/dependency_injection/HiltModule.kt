package com.example.composeapp.dependency_injection

import com.example.composeapp.data.data_source.CommentDataSource
import com.example.composeapp.data.data_source.LikesDataSource
import com.example.composeapp.data.data_source.PostDataSource
import com.example.composeapp.data.data_source.UserDataSource
import com.example.composeapp.data.repositories.CommentsRepositoryImpl
import com.example.composeapp.data.repositories.LikesRepositoryImpl
import com.example.composeapp.data.repositories.PostsRepositoryImpl
import com.example.composeapp.data.repositories.UserRepositoryImpl
import com.example.composeapp.domain.repository.CommentsRepository
import com.example.composeapp.domain.repository.LikesRepository
import com.example.composeapp.domain.repository.PostRepository
import com.example.composeapp.domain.repository.UserRepository
import com.example.composeapp.domain.use_case.GetPassedTimeUseCase
import com.example.composeapp.domain.use_case.GetPostCommentsWithUserUseCase
import com.example.composeapp.domain.use_case.GetRecentPostsWithUserUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object HiltModule {

    @Provides
    @Singleton
    fun providesUserRepository(
        userDataSource: UserDataSource
    ) : UserRepository {
        return UserRepositoryImpl(userDataSource)
    }

    @Provides
    @Singleton
    fun providesLikesRepository(
        likesDataSource: LikesDataSource
    ) : LikesRepository {
        return LikesRepositoryImpl(likesDataSource)
    }

    @Provides
    @Singleton
    fun providesPostsRepository(
        postDataSource: PostDataSource
    ) : PostRepository {
        return PostsRepositoryImpl(postDataSource)
    }

    @Provides
    @Singleton
    fun providesCommentsRepository(
        commentDataSource: CommentDataSource
    ) : CommentsRepository {
        return CommentsRepositoryImpl(commentDataSource)
    }

    @Provides
    @Singleton
    fun providesGetPostCommentsWithUserUseCase(
        userRepository: UserRepository,
        commentsRepository: CommentsRepository,
        likesRepository: LikesRepository
    ) : GetPostCommentsWithUserUseCase {
        return GetPostCommentsWithUserUseCase(
            userRepository = userRepository,
            commentsRepository = commentsRepository,
            likesRepository = likesRepository
        )
    }

    @Provides
    @Singleton
    fun providesGetRecentPostsWithUserUseCase(
        userRepository: UserRepository,
        postRepository: PostRepository,
        likesRepository: LikesRepository
    ) : GetRecentPostsWithUserUseCase{
        return GetRecentPostsWithUserUseCase(
            userRepository = userRepository,
            postRepository = postRepository,
            likesRepository = likesRepository,
            getPassedTime = GetPassedTimeUseCase()
        )
    }
}