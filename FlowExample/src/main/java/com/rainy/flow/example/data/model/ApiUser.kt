package com.rainy.flow.example.data.model

import com.google.gson.annotations.SerializedName

/**
 * @author jiangshiyu
 * @date 2023/4/10
 */
data class ApiUser(

    @SerializedName("id")
    val id: Int = 0,

    @SerializedName("name")
    val name: String,

    @SerializedName("email")
    val email: String,

    @SerializedName("avatar")
    val avatar: String
)