package com.rainy.android_opensource_practice.user

import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.rainy.android_opensource_practice.BaseActivity
import com.rainy.android_opensource_practice.databinding.ActivityUserBinding
import kotlinx.coroutines.flow.collect

/**
 * @author jiangshiyu
 * @date 2022/10/14
 */
class UserActivity : BaseActivity<UserViewModel, ActivityUserBinding>() {

    override fun initView(savedInstanceState: Bundle?) {
        mViewBind.apply {
            addUser.setOnClickListener {
                mViewModel.insert(
                    etUserId.text.toString(),
                    etFirstName.text.toString(),
                    etSecondName.text.toString()
                )
            }
        }


        val adapter = UserAdapter()
        mViewBind.recycler.apply {
            this.layoutManager = LinearLayoutManager(this@UserActivity)
            this.adapter = adapter
        }
        lifecycleScope.launchWhenCreated {
            mViewModel.getAll().collect {
                adapter.addData(it.toMutableList())
            }
        }


    }
}