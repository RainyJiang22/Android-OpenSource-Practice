package com.rainy.flow.example.flowtest

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

/**
 * @author jiangshiyu
 * @date 2022/11/3
 */
class FlowViewModel : ViewModel() {


    //可以使用sharedFlow处理多个事件
    private val _showToast = MutableSharedFlow<String>()

    val showToast: MutableSharedFlow<String>
        get() = _showToast

    fun showToast() {
        viewModelScope.launch {
            _showToast.emit("SharedFlow Hello")
            _showToast.emit("SharedFlow Toast")
        }
    }
}