package com.example.composeapp.data.repositories

import com.example.composeapp.data.data_source.UserDataSource
import com.example.composeapp.domain.model.User
import com.example.composeapp.domain.repository.UserRepository

class UserRepositoryImpl(
    private val userDataSource: UserDataSource
) : UserRepository {
    override suspend fun getUser(id: Int): User? {
        return userDataSource.getUser(id)
    }
}