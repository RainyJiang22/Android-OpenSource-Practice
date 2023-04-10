package com.rainy.flow.example.data.local

import com.rainy.flow.example.data.local.entity.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * @author jiangshiyu
 * @date 2023/4/10
 */
class DataBaseHelperImpl(private val appDataBase: AppDataBase) : DatabaseHelper {
    override fun getUsers(): Flow<List<User>> {
        return flow { emit(appDataBase.userDao().getAll()) }
    }

    override fun insertAll(user: List<User>): Flow<Unit> {
        return flow { emit(appDataBase.userDao().insertAll(user)) }
    }
}