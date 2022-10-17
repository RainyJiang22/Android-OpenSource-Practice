package com.rainy.android_opensource_practice.number

import kotlinx.coroutines.flow.MutableStateFlow
import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel

/**
 * @author jiangshiyu
 * @date 2022/10/17
 */
class NumberViewModel : BaseViewModel() {


    val number = MutableStateFlow(0)

    fun add() {
        number.value++
    }
    fun remove() {
        number.value--
    }
}