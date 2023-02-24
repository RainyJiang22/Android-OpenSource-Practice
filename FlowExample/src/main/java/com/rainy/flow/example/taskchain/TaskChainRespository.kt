package com.rainy.flow.example.taskchain

import android.content.Context
import kotlinx.coroutines.flow.MutableStateFlow

/**
 * @author jiangshiyu
 * @date 2023/2/24
 */
class TaskChainRepository {


    val taskMap = linkedMapOf(
        1 to TaskOne(),
        2 to TaskTwo(),
        3 to TaskThree()
    )

    //优先级
    private var currLevel = 1

    private val _stateTask = MutableStateFlow(currLevel)
    val stateTask get() = _stateTask

    fun doNewTask(context: Context, task: ChainTask) {
        if (task.execute()) {
            task.launch(context) {
                currLevel++
                if (currLevel <= taskMap.size) {
                    _stateTask.value = currLevel
                }
            }
        } else {
            currLevel++
            if (currLevel <= taskMap.size) {
                _stateTask.value = currLevel
            }
        }
    }
}