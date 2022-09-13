package com.rainy.monitor.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.rainy.monitor.db.HttpInformation
import com.rainy.monitor.db.MonitorHttpInformationDataBase

/**
 * @author jiangshiyu
 * @date 2022/9/13
 */
class MonitorDetailsViewModel(application: Application, id: Long) : AndroidViewModel(application) {


    val recordLiveData by lazy {
        MonitorHttpInformationDataBase.INSTANCE.httpInformationDao.queryRecordObservable(id)
    }


}