package com.rainy.flow.example.data.local

import android.content.Context
import androidx.room.Room

/**
 * @author jiangshiyu
 * @date 2023/4/10
 */
object DatabaseBuilder {

    private var INSTANCE: AppDataBase? = null

    fun getInstance(context: Context): AppDataBase {

        if (INSTANCE == null) {
            synchronized(AppDataBase::class) {
                if (INSTANCE == null) {
                    INSTANCE = buildRoomDB(context)
                }
            }
        }
        return INSTANCE!!
    }

    private fun buildRoomDB(context: Context) =
        Room.databaseBuilder(
            context.applicationContext,
            AppDataBase::class.java,
            "userdatabase")
            .build()
}