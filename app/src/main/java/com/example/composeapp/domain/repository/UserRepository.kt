package com.example.composeapp.domain.repository

import com.example.composeapp.domain.model.User

interface UserRepository {
    suspend fun getUser(id:Int) : User?
}