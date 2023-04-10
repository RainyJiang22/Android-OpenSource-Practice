package com.rainy.android_opensource_practice.number

import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.rainy.android_opensource_practice.BaseActivity
import com.rainy.android_opensource_practice.databinding.ActivityFlowStateBinding
import kotlinx.coroutines.flow.collect

/**
 * @author jiangshiyu
 * @date 2022/10/17
 */
class NumberActivity : BaseActivity<NumberViewModel, ActivityFlowStateBinding>() {


    override fun initView(savedInstanceState: Bundle?) {

        mViewBind?.apply {

            btnAdd.setOnClickListener {
                mViewModel.add()
            }

            btnDecrement.setOnClickListener {
                mViewModel.remove()
            }
        }
    }

    override fun createObserver() {
        lifecycleScope.launchWhenCreated {

            mViewModel.number.collect {
                mViewBind?.textValue?.text = it.toString()
            }
        }
    }

    override fun onBundle(bundle: Bundle) {
    }
}