package com.rainy.mvi.model.bean

/**
 * @author jiangshiyu
 * @date 2023/1/11
 */
data class Article(
    var curPage: Int,
    val datas: List<ArticleItem>
)

data class ArticleItem(
    val title: String,
    val link: String,
    val userId: Int,
    val niceDate: String
)