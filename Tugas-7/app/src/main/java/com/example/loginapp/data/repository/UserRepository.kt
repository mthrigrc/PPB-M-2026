package com.example.loginapp.data.repository

import com.example.loginapp.data.local.User
import com.example.loginapp.data.local.UserDao

class UserRepository(private val userDao: UserDao) {
    suspend fun seedDemoUser() {
        if (userDao.findByUsername("admin") == null) {
            userDao.insertUser(User(username = "admin", password = "12345"))
        }
    }

    suspend fun login(username: String, password: String): Boolean =
        userDao.login(username.trim(), password) != null
}
