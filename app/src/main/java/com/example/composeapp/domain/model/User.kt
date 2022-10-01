package com.example.composeapp.domain.model

data class User(
    val id:Int = -1,
    val firstName:String = "",
    val lastName:String = "",
    val profilePicUrl:String = ""
){
    val fullName get() = "$firstName $lastName"
}
