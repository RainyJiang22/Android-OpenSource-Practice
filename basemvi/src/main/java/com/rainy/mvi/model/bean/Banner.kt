package com.rainy.mvi.model.bean

/**
 * @author jiangshiyu
 * @date 2023/1/11
 */
data class Banner(
    var desc: String,
    var imagePath: String,
    var title: String,
    val url: String,
)
