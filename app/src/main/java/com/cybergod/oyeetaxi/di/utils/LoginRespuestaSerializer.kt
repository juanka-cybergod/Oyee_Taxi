package com.cybergod.oyeetaxi.di.utils

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import com.cybergod.oyeetaxi.api.model.response.LoginRespuesta
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.OutputStream

object LoginRespuestaSerializer : Serializer<LoginRespuesta> {

    override val defaultValue: LoginRespuesta
        get() = LoginRespuesta()

    override suspend fun readFrom(input: InputStream): LoginRespuesta {
        return try {
            Json.decodeFromString(
                deserializer = LoginRespuesta.serializer(),
                string = input.readBytes().decodeToString()
            )
        } catch (e: SerializationException){
            defaultValue
            throw CorruptionException("Archivo Proto Data Store Corrupto",e)
        }
    }

    @Suppress("UseExpressionBody")
    override suspend fun writeTo(t: LoginRespuesta, output: OutputStream) {
        output.write(
            Json.encodeToString(
                serializer = LoginRespuesta.serializer(),
                value = t
            ).encodeToByteArray()
        )
    }
}