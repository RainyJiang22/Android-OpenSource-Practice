package com.rainy.android_opensource_practice.datastore

import android.content.Context
import android.os.Bundle
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import com.rainy.android_opensource_practice.BaseActivity
import com.rainy.android_opensource_practice.databinding.ActivityDataStoreBinding
import com.rainy.datastore.PersonPreferences
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel

/**
 * @author jiangshiyu
 * @date 2022/11/1
 */
class DataStoreActivity : BaseActivity<BaseViewModel, ActivityDataStoreBinding>() {


    //创建 DataStore
    val Context.studyDataStore: DataStore<PersonPreferences> by dataStore(
        fileName = "study.pb",
        serializer = PersonSerializer
    )


    override fun initView(savedInstanceState: Bundle?) {

        mViewBind.btnSave.setOnClickListener {
            EasyDataStore.putData("name", "居家")
        }

        mViewBind.btnRead.setOnClickListener {
            val data = EasyDataStore.getData("name", "办公")
            mViewBind.tvResult.text = data
        }

        mViewBind.btnSave.setOnClickListener {
            EasyDataStore.clearData()
        }

        mViewBind.btnProtoSave.setOnClickListener {
            runBlocking {
                studyDataStore.updateData {
                    it.toBuilder()
                        .setName("刘爱国")
                        .setAge(31)
                        .build()
                }
            }
        }

        mViewBind.btnProtoRead.setOnClickListener {
            runBlocking {
                val person = studyDataStore.data.first()
                mViewBind.tvResult.text = "name: ${person.name} , age: ${person.age}"
            }
        }
    }

    override fun createObserver() {
    }
}