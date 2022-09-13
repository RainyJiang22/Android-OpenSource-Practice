package com.rainy.monitor.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import com.rainy.monitor.db.MonitorHttpInformationDataBase

/**
 * @author jiangshiyu
 * @date 2022/9/13
 */
class MonitorViewModel(application: Application) : AndroidViewModel(application) {

    companion object {

        private const val LIMIT = 300
    }

    val allRecordLiveData by lazy {
        MonitorHttpInformationDataBase.INSTANCE.httpInformationDao.queryAllRecordObservable(
            LIMIT
        )
    }

}