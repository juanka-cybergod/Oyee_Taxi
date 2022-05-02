package com.cybergod.oyeetaxi.api.utils


import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okio.BufferedSink
import java.io.File
import java.io.FileInputStream


class UploadRequestBodyNoProgress(
    private val file: File,
    private val contentType:String,
    ):RequestBody() {


    override fun contentType()  = "$contentType/*".toMediaTypeOrNull()
    override fun contentLength() = file.length()


    override fun writeTo(sink: BufferedSink) {
        val buffer =  ByteArray(DEFAULT_BUFFER_SIZE)
        val fileImputStream = FileInputStream(file)
        var uploaded = 0L
        fileImputStream.use { imputStream ->
            var readed: Int


            while (imputStream.read(buffer).also {  readed = it } != -1 ){
                uploaded += readed
                sink.write(buffer, 0, readed)
            }



        }
    }



    companion object{
        private const val DEFAULT_BUFFER_SIZE = 1024
    }



}