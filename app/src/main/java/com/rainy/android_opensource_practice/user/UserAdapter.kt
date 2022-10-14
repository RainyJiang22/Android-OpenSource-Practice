package com.rainy.android_opensource_practice.user

import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.rainy.android_opensource_practice.R

/**
 * @author jiangshiyu
 * @date 2022/10/14
 */
class UserAdapter : BaseQuickAdapter<User, BaseViewHolder>(R.layout.item_user) {
    override fun convert(holder: BaseViewHolder, item: User) {

        holder.getView<TextView>(R.id.txt_first).text = item.firstName
        holder.getView<TextView>(R.id.txt_second).text = item.lastName
    }
}
