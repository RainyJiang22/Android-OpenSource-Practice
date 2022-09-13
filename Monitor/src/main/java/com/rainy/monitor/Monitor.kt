package com.rainy.monitor

import android.content.Context
import android.content.Intent
import androidx.lifecycle.LiveData
import com.rainy.monitor.db.HttpInformation
import com.rainy.monitor.db.MonitorHttpInformationDataBase
import com.rainy.monitor.ui.MonitorActivity
import kotlin.concurrent.thread

/**
 * @author jiangshiyu
 * @date 2022/9/13
 */
internal object Monitor {

    fun getLaunchIntent(context: Context): Intent {
        val intent = Intent(context, MonitorActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        return intent
    }

    fun clearCache() {
        thread {
           MonitorHttpInformationDataBase.INSTANCE.httpInformationDao.deleteAll()
        }
    }

    fun queryAllRecord(limit: Int): LiveData<List<HttpInformation>> {
        return MonitorHttpInformationDataBase.INSTANCE.httpInformationDao.queryAllRecordObservable(
            limit
        )
    }

    fun queryAllRecord(): LiveData<List<HttpInformation>> {
        return MonitorHttpInformationDataBase.INSTANCE.httpInformationDao.queryAllRecordObservable()
    }
}