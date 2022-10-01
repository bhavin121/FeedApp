package com.example.composeapp.domain.model

data class Post(
    val id:Int = -1,
    val userId:Int = -1,
    val timeStamp:Long = 0L,
    val postType: PostType = PostType.QUESTION,
    val bodyText:String? = null,
    val numLikes:Int = 0,
    val numComments:Int = 0,
    val media: Media? = null
)