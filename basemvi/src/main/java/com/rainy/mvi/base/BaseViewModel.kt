package com.rainy.mvi.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rainy.mvi.IUIState
import com.rainy.mvi.IUiIntent
import com.rainy.mvi.LoadUiIntent
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * @author jiangshiyu
 * @date 2023/1/10
 */
abstract class BaseViewModel<UiState : IUIState, UiIntent : IUiIntent> : ViewModel() {

    private val _uiStateFlow = MutableStateFlow(initUiState())

    private val _uiIntentFlow: Channel<UiIntent> = Channel()
    val uiIntentFlow: Flow<UiIntent> = _uiIntentFlow.receiveAsFlow()

    private val _loadUiIntentFlow: Channel<LoadUiIntent> = Channel()
    val loadUiIntentFlow: Flow<LoadUiIntent> = _loadUiIntentFlow.receiveAsFlow()


    protected abstract fun initUiState(): UiState

    protected fun sendUiState(copy: UiState.() -> UiState) {
        _uiStateFlow.update { copy(_uiStateFlow.value) }
    }

    fun sendUiIntent(uiIntent: UiIntent) {
        viewModelScope.launch {
            _uiIntentFlow.send(uiIntent)
        }
    }

    init {
        viewModelScope.launch {
            uiIntentFlow.collect {
                handleIntent(it)
            }
        }
    }

    protected abstract fun handleIntent(intent: IUiIntent)


    /**
     * 发送当前加载状态
     */
    private fun sendLoadUiIntent(loadUiIntent: LoadUiIntent) {
        viewModelScope.launch {
            _loadUiIntentFlow.send(loadUiIntent)
        }
    }

    protected fun <T : Any> requestDataWithFlow(
        showLoading: Boolean = true,
        request: suspend () -> BaseData<T>,
        successCallback: (T) -> Unit,
        failCallback: suspend (String) -> Unit = { errorMsg ->
            //默认异常处理，子类可以进行编写
            sendLoadUiIntent(LoadUiIntent.Error(errorMsg))
        },
    ) {
        viewModelScope.launch {
            //是否展示loading
            if (showLoading) {
                sendLoadUiIntent(LoadUiIntent.Loading(true))
            }
            val baseData: BaseData<T>
            try {

                baseData = request()
                when (baseData.state) {
                    ReqState.SUCCESS -> {
                        sendLoadUiIntent(LoadUiIntent.ShowMainView)
                    }
                    ReqState.ERROR -> {
                        baseData.msg?.let { error(it) }
                    }
                }

            } catch (e: Exception) {
                e.message?.let { failCallback(it) }
            } finally {
                if (showLoading) {
                    sendLoadUiIntent(LoadUiIntent.Loading(false))
                }
            }
        }
    }

}