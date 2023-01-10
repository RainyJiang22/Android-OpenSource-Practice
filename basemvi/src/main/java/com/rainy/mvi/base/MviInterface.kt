package com.rainy.mvi

import androidx.annotation.Keep

/**
 * @author jiangshiyu
 * @date 2023/1/10
 */

@Keep
interface IUIState

@Keep
interface IUiIntent


@Keep
sealed class LoadUiIntent {

    data class Loading(var isShow: Boolean) : LoadUiIntent()

    object ShowMainView : LoadUiIntent()

    data class Error(val msg: String) : LoadUiIntent()
}