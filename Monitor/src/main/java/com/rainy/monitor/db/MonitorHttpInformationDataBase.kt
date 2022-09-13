package com.rainy.monitor.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.rainy.monitor.holder.ContextHolder

/**
 * @author jiangshiyu
 * @date 2022/9/13
 */
@Database(entities = [HttpInformation::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
internal abstract class MonitorHttpInformationDataBase : RoomDatabase() {

    abstract val httpInformationDao: MonitorHttpInformationDao


    companion object {
        private const val DB_NAME = "MonitorHttpInformation.db"

        val INSTANCE: MonitorHttpInformationDataBase by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
            create(ContextHolder.context)
        }

        private fun create(context: Context): MonitorHttpInformationDataBase {
            return Room.databaseBuilder(
                context,
                MonitorHttpInformationDataBase::class.java,
                DB_NAME
            ).fallbackToDestructiveMigration()
                .build()
        }
    }


}