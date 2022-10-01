package com.example.composeapp.data.data_source

import com.example.composeapp.domain.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * This is a fake user data source
 */
class UserDataSource @Inject constructor(){
    private val users = listOf(
        User(
            id = 0,
            firstName = "Akash",
            lastName = "Kumar",
            profilePicUrl = "https://upload.wikimedia.org/wikipedia/en/thumb/8/8d/Robert_Downey_Jr._as_Tony_Stark_in_Iron_Man_3.jpg/170px-Robert_Downey_Jr._as_Tony_Stark_in_Iron_Man_3.jpg"
        ),
        User(
            id = 1,
            firstName = "Tarun",
            lastName = "Singh",
            profilePicUrl = "https://image.shutterstock.com/image-photo/young-handsome-man-beard-wearing-260nw-1768126784.jpg"
        ),
        User(
            id = 2,
            firstName = "Aditya",
            lastName = "Rathod",
            profilePicUrl = "https://images.unsplash.com/photo-1552642986-ccb41e7059e7?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxzZWFyY2h8MXx8aGFuZHNvbWUlMjBtYW58ZW58MHx8MHx8&w=1000&q=80"
        ),
        User(
            id = 3,
            firstName = "Suraj",
            lastName = "Kumar",
            profilePicUrl = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTOKgf1btqasr29ElrKVN80RPZxRDi4qTMnwQ&usqp=CAU"
        ),
        User(
            id = 4,
            firstName = "Bhavin",
            lastName = "Prajapat",
            profilePicUrl = "https://img.freepik.com/premium-photo/oh-my-god-portrait-astonished-handsome-man-denim-casual-shirt-looking-camera-with-big-amazed-eyes-saying-wow-shocked-by-unbelievable-news-indoor-studio-shot-isolated-yellow-background_416530-21128.jpg?w=2000"
        ),
    )

    suspend fun getUser(id:Int) : User? = withContext(Dispatchers.Default){
        users.find { it.id == id }
    }
}