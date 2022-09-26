package com.rainy.android_opensource_practice

import com.rainy.easybus.EventLiveData
import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel

/**
 * @author jiangshiyu
 * @date 2022/9/26
 * 全局App的ViewModel,这里去替代Eventbus或者是LiveDataBus
 */
class EventViewModel : BaseViewModel() {

    var textEvent = EventLiveData<HelloBean>()
}