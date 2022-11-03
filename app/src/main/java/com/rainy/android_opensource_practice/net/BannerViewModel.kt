package com.rainy.android_opensource_practice.net

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.rainy.flow.example.retrofit.RetrofitClient
import com.rainy.flow.example.retrofit.data.BannerResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel

/**
 * @author jiangshiyu
 * @date 2022/10/17
 */
class BannerViewModel : BaseViewModel() {


    val bannerDataState = MutableLiveData<ArrayList<BannerResponse>>()

    fun getBanner() {
        viewModelScope.launch {
            flow {
                val data = RetrofitClient.articleApi.getBanner()
                //发射出去数据
                emit(data.data)
            }.flowOn(Dispatchers.IO) //转换成IO子线程中进行
                .catch {
                    it.printStackTrace()
                }
                .collect {
                    bannerDataState.value = it
                }
        }

    }
}