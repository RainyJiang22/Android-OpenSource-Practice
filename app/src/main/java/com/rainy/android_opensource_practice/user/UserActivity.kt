package com.rainy.android_opensource_practice.user

import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.rainy.android_opensource_practice.BaseActivity
import com.rainy.android_opensource_practice.databinding.ActivityUserBinding
import kotlinx.coroutines.flow.collect
import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel

/**
 * @author jiangshiyu
 * @date 2022/10/14
 */
class UserActivity : BaseActivity<BaseViewModel, ActivityUserBinding>() {


    private val userViewModel: UserViewModel by viewModels()


    override fun initView(savedInstanceState: Bundle?) {
        mViewBind.apply {
            addUser.setOnClickListener {
                userViewModel.insert(
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
            userViewModel.getAll().collect {
                adapter.addData(it.toMutableList())
            }
        }


    }
}