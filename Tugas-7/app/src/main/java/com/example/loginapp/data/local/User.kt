package com.example.loginapp.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val username: String,
    // Demo-only plaintext password. Real apps must store a salted password hash.
    val password: String
)
