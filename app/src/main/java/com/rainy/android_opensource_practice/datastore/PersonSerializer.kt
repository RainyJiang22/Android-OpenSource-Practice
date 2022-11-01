package com.rainy.android_opensource_practice.datastore

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import com.google.protobuf.InvalidProtocolBufferException
import com.rainy.datastore.PersonPreferences
import java.io.InputStream
import java.io.OutputStream

/**
 * @author jiangshiyu
 * @date 2022/11/1
 */
object PersonSerializer : Serializer<PersonPreferences> {

    override val defaultValue: PersonPreferences = PersonPreferences.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): PersonPreferences {
        try {
            return PersonPreferences.parseFrom(input)
        } catch (exception: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read proto.", exception)
        }
    }

    override suspend fun writeTo(t: PersonPreferences, output: OutputStream) = t.writeTo(output)
}
