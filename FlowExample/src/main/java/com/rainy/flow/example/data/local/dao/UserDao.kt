package com.rainy.flow.example.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.rainy.flow.example.data.local.entity.User

/**
 * @author jiangshiyu
 * @date 2023/4/10
 */

@Dao
interface UserDao {

    @Query("select * from user")
    fun getAll(): List<User>

    @Insert
    fun insertAll(users: List<User>)

    @Delete
    fun delete(user: User)
}