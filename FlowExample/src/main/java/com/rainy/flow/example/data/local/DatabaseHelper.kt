package com.rainy.flow.example.data.local

import com.rainy.flow.example.data.local.entity.User
import kotlinx.coroutines.flow.Flow

/**
 * @author jiangshiyu
 * @date 2023/4/10
 */
interface DatabaseHelper {

    fun getUsers(): Flow<List<User>>

    fun insertAll(user: List<User>): Flow<Unit>
}