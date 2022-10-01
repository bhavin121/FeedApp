package com.example.composeapp.presentation.feed

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.composeapp.domain.model.PostWithUser
import com.example.composeapp.domain.use_case.GetPostCommentsWithUserUseCase
import com.example.composeapp.domain.use_case.GetRecentPostsWithUserUseCase
import com.example.composeapp.presentation.UiEvent
import com.example.composeapp.presentation.feed.ui_state.PostCommentsUiState
import com.example.composeapp.presentation.feed.ui_state.PostFeedsUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostFeedViewModel @Inject constructor(
    private val getRecentPostsWithUser: GetRecentPostsWithUserUseCase,
    private val getPostCommentsWithUser: GetPostCommentsWithUserUseCase
) : ViewModel(){

    private val className: String = PostFeedViewModel::class.java.simpleName

    private val _postUiState = MutableLiveData(PostFeedsUiState())
    val postUiState : LiveData<PostFeedsUiState> = _postUiState

    private val _commentUiState = MutableLiveData<PostCommentsUiState>(PostCommentsUiState())
    val commentUiState : LiveData<PostCommentsUiState> = _commentUiState

    private var viewCommentsOfPost:PostWithUser? = null

    private var nextPostPageId:Int = 0
    private var nextCommentPageId:Int = 0

    private var fetchPostJob:Job? = null
    private var fetchCommentsJob:Job? = null

    init {
        loadPosts()
    }

    fun onEvent(event: UiEvent){
        when(event){
            is UiEvent.LoadMorePost -> {
                Log.d(className, "Load more event")
                loadPosts()
            }
            is UiEvent.ChangeCurrentVideo -> {
                Log.d(className, "Change current video event")
                if(postUiState.value?.currentPlayingVideo != event.current){
                    val newState = PostFeedsUiState(
                        posts = postUiState.value?.posts ?: emptyList(),
                        currentPlayingVideo = event.current
                    )
                    _postUiState.value = newState
                }
            }
            is UiEvent.LoadMoreComment -> {
                Log.d(className, "Load more comment event")
                loadComments()
            }
            is UiEvent.ViewPostComments -> {
                Log.d(className, "View comments event")
                viewCommentsOfPost = event.postWithUser
                nextCommentPageId = 0
                _commentUiState.postValue(PostCommentsUiState())
                loadComments()
            }
        }
    }

    private fun loadPosts() {
        fetchPostJob?.cancel()
        fetchPostJob = viewModelScope.launch {
            // Change loading state
            _postUiState.postValue(postUiState.value?.copy(isLoading = true))
            val postWithUsersPage = getRecentPostsWithUser(pageId = nextPostPageId)
            val newUiState = postUiState.value?.copy(
                posts = _postUiState.value?.let {
                    it.posts.toMutableList().apply {
                        addAll(postWithUsersPage.postsWithUser)
                    }
                }?:postWithUsersPage.postsWithUser,
                isLoading = false
            )
            // Update ui state
            _postUiState.postValue(newUiState)
            nextPostPageId = postWithUsersPage.nextPage
        }
    }

    private fun loadComments(){
        fetchCommentsJob?.cancel()
        fetchCommentsJob = viewModelScope.launch {
            // Change loading state
            _commentUiState.postValue(commentUiState.value?.copy(isLoading = true))

            val commentWithUserPage = getPostCommentsWithUser(viewCommentsOfPost!!.post.id, nextCommentPageId)
            val newUiState = commentUiState.value?.copy(
                comments = commentUiState.value?.let {
                    it.comments.toMutableList().apply {
                        addAll(commentWithUserPage.commentsWithUser)
                    }
                }?: emptyList(),
                isLoading = false,
                post = viewCommentsOfPost
            )
            // Update ui state
            _commentUiState.postValue(newUiState)
            nextCommentPageId = commentWithUserPage.nextPage
        }
    }
}