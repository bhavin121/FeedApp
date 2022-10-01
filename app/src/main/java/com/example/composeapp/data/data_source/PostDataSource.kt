package com.example.composeapp.data.data_source

import com.example.composeapp.domain.model.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import java.util.Calendar
import javax.inject.Inject

class PostDataSource @Inject constructor(){

    enum class MediaType {
        VIDEO,PHOTO
    }

    private val photos = listOf(
        "https://cdn.pixabay.com/photo/2015/04/23/22/00/tree-736885__480.jpg",
        "https://media.istockphoto.com/photos/mountain-landscape-picture-id517188688?k=20&m=517188688&s=612x612&w=0&h=i38qBm2P-6V4vZVEaMy_TaTEaoCMkYhvLCysE7yJQ5Q=",
        "https://images.unsplash.com/photo-1503023345310-bd7c1de61c7d?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxzZWFyY2h8MXx8aHVtYW58ZW58MHx8MHx8&w=1000&q=80"
    )

    private val videos = listOf(
        Media.Video(
            "https://firebasestorage.googleapis.com/v0/b/test-46e94.appspot.com/o/Woman%20-%2058142.mp4?alt=media&token=b70358c3-d86e-45ed-9f9f-6180106ea589",
            640.toDouble() / 360,
            "https://firebasestorage.googleapis.com/v0/b/test-46e94.appspot.com/o/Woman%20-%2058142_Moment.jpg?alt=media&token=b5584ddc-b462-4b16-8235-5487c44cfbb6"
        ),
        Media.Video(
            "https://firebasestorage.googleapis.com/v0/b/test-46e94.appspot.com/o/production%20ID_3825923.mp4?alt=media&token=c7611517-5b23-4378-a308-156eaa79a897",
            426.toDouble() / 240,
            "https://firebasestorage.googleapis.com/v0/b/test-46e94.appspot.com/o/production%20ID_3825923_Moment.jpg?alt=media&token=ae6fe714-301e-4862-9384-017f1a43446e"
        )
    )

    private val postContents = listOf(
        "What is urvar?",
        "Just landed on urvar",
        "Agriculture or farming is the practice of cultivating plants and livestock"
    )

    private val postTypes = listOf(
        PostType.MARKETING,
        PostType.QUESTION
    )

    private val mediaTypes = listOf(
        MediaType.PHOTO,
        MediaType.VIDEO
    )

    private val timeStamps = listOf(
        Calendar.getInstance().apply {
            set(Calendar.YEAR, 2021)
        }.timeInMillis,
        Calendar.getInstance().apply {
            set(Calendar.MONTH, 7)
        }.timeInMillis,
        Calendar.getInstance().apply {
            set(Calendar.DATE, 20)
        }.timeInMillis,
        Calendar.getInstance().apply {
            set(Calendar.YEAR, 2020)
        }.timeInMillis,
        Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 8)
        }.timeInMillis
    )

    suspend fun getPosts(pageId:Int) : PostsPage = withContext(Dispatchers.Default){
        delay(1500)
        val posts = mutableListOf<Post>()
        val pageSize = 20

        repeat(20){ i ->
            val post = Post(
                id = pageId*pageSize + i,
                userId = (0..4).random(),
                timeStamp = timeStamps.random(),
                postType = postTypes.random(),
                bodyText = postContents.random(),
                numLikes = (1..500).random(),
                numComments = (1..50).random(),
                media = randomMedia()
            )
            posts.add(post)
        }

        PostsPage(posts = posts, nextPage = pageId+1)
    }

    private fun randomMedia(): Media? {
        if((1..3).random() == 3) return null
        return when(mediaTypes.random()){
            MediaType.VIDEO -> videos.random()
            else -> {
                val numPhoto = (1..3).random()
                Media.Photo(List(numPhoto){photos[it]})
            }
        }
    }
}