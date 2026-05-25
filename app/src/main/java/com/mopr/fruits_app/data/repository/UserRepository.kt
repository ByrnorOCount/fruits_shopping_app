package com.mopr.fruits_app.data.repository

import com.mopr.fruits_app.data.remote.FirestoreManager
import com.mopr.fruits_app.data.model.User

class UserRepository(private val firestore: FirestoreManager) {
    suspend fun login(id: String, pass: String) = firestore.loginUser(id, pass)
    suspend fun register(user: User) = firestore.registerUser(user)
    fun logout() = firestore.logout()
}