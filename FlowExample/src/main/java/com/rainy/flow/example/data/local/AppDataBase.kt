package com.rainy.flow.example.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.rainy.flow.example.data.local.dao.UserDao
import com.rainy.flow.example.data.local.entity.User

/**
 * @author jiangshiyu
 * @date 2023/4/10
 */

@Database(entities = [User::class], version = 1)
abstract class AppDataBase : RoomDatabase() {

    abstract fun userDao(): UserDao
}