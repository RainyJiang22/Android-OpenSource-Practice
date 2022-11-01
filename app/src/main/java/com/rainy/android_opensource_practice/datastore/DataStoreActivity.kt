package com.rainy.android_opensource_practice.datastore

import android.os.Bundle
import com.rainy.android_opensource_practice.BaseActivity
import com.rainy.android_opensource_practice.databinding.ActivityDataStoreBinding
import com.rainy.easy.datastore.EasyDataStore
import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel

/**
 * @author jiangshiyu
 * @date 2022/11/1
 */
class DataStoreActivity : BaseActivity<BaseViewModel, ActivityDataStoreBinding>() {


    override fun initView(savedInstanceState: Bundle?) {

        mViewBind.btnSave.setOnClickListener {
            EasyDataStore.putData("name", "居家")
        }

        mViewBind.btnRead.setOnClickListener {
            val data = EasyDataStore.getData("name", "办公")
            mViewBind.tvResult.text = data
        }

        mViewBind.btnSave.setOnClickListener {
           //EasyDataStore.clearData()
        }
    }

    override fun createObserver() {
    }
}