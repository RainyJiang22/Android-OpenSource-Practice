package com.rainy.rxlivedatahttp

import androidx.lifecycle.LiveData
import com.rainy.rxlivedatahttp.bean.HttpWrapBean
import retrofit2.http.GET

/**
 * @author jiangshiyu
 * @date 2022/9/13
 */
interface ApiInterface {

    @GET("getUserData")
    fun getUserData(): LiveData<HttpWrapBean<UserBean>>
}

data class UserBean(val userName: String, val userAge: Int)