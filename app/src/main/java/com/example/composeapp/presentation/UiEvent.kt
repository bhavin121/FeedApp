package com.example.composeapp.presentation

import com.example.composeapp.domain.model.PostWithUser

sealed class UiEvent {
    object LoadMorePost : UiEvent()
    object LoadMoreComment : UiEvent()
    class ViewPostComments(val postWithUser: PostWithUser) : UiEvent()
    class ChangeCurrentVideo(val current:PostWithUser?) : UiEvent()
}