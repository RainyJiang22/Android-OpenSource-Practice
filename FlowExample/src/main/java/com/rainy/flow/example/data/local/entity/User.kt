package com.rainy.flow.example.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * @author jiangshiyu
 * @date 2023/4/10
 */

@Entity
data class User(
    @PrimaryKey
    val id: Int,

    @ColumnInfo(name = "name")
    val name: String?,

    @ColumnInfo(name = "email")
    val email: String?,

    @ColumnInfo(name = "avatar")
    val avatar: String?
)