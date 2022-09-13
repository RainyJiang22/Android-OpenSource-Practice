package com.rainy.monitor.db

import androidx.room.TypeConverter
import com.rainy.monitor.data.HttpHeader
import com.rainy.monitor.utils.SerializableHolder
import java.util.*

/**
 * @author jiangshiyu
 * @date 2022/9/13
 */
internal class Converters {

    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return if (value == null) null else Date(value)
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }

    @TypeConverter
    fun toHttpHeaderList(json: String): List<HttpHeader> {
        return SerializableHolder.fromJsonArray(json, HttpHeader::class.java)
    }

    @TypeConverter
    fun toJsonFromHttpHeaderList(list: List<HttpHeader>): String {
        return SerializableHolder.toJson(list)
    }
}