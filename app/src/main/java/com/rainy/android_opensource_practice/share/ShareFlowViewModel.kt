package com.rainy.android_opensource_practice.share

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import okhttp3.Dispatcher

/**
 * @author jiangshiyu
 * @date 2022/10/17
 */
class ShareFlowViewModel : ViewModel() {

    private lateinit var job: Job


    fun startRefresh() {
        job = viewModelScope.launch(Dispatchers.IO) {
            while (true) {
                LocalEventBus.postEvent(Event(System.currentTimeMillis()))
            }
        }
    }

    fun stopRefresh() {
        job.cancel()
    }

}