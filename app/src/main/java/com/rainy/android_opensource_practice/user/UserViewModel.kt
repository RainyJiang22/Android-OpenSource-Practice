package com.rainy.android_opensource_practice.user

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.rainy.android_opensource_practice.BaseApp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import me.hgj.jetpackmvvm.base.appContext
import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel

/**
 * @author jiangshiyu
 * @date 2022/10/14
 */
class UserViewModel : BaseViewModel() {


    fun insert(uid: String, firstName: String, lastName: String) {
        viewModelScope.launch {
            AppDatabase.getInstance(BaseApp.getContext()!!.applicationContext)
                .userDao()
                .insert(User(uid.toInt(), firstName, lastName))
            Log.d("database", "insert user:$uid")
        }
    }

    fun getAll(): Flow<List<User>> {
        return AppDatabase.getInstance(BaseApp.getContext()!!.applicationContext)
            .userDao()
            .getAll()
            .catch { e -> e.printStackTrace() }
            .flowOn(Dispatchers.IO) //切换上下文为IO异步
    }
}