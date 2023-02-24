package com.rainy.flow.example.taskchain

import android.content.Context

/**
 * @author jiangshiyu
 * @date 2023/2/24
 */
interface ChainTask {

    //判断任务是否执行
    fun execute(): Boolean

    //任务启动
    fun launch(context: Context, callback: () -> Unit)

}