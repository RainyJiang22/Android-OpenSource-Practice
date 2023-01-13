package com.rainy.android_opensource_practice.modularbus

import com.pengxr.modular.eventbus.facade.annotation.EventGroup

/**
 * @author jiangshiyu
 * @date 2023/1/13
 */

@EventGroup
interface LoginEvents {

    fun login(): UserInfo

    //登出
    fun logout()

}