package com.cybergod.oyeetaxi.api.utils


import android.os.Handler
import android.os.Looper
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okio.BufferedSink
import java.io.File
import java.io.FileInputStream


class UploadRequestBody(
    private val file: File,
    private val contentType:String,
    private val callback: UploadCallback
    ):RequestBody() {


    interface UploadCallback{
        fun onProggressUpdate(porcentage : Int)
    }


    override fun contentType()  = "$contentType/*".toMediaTypeOrNull()
    override fun contentLength() = file.length()


    override fun writeTo(sink: BufferedSink) {
        val lengthTotal = file.length()
        val buffer =  ByteArray(DEFAULT_BUFFER_SIZE)
        val fileImputStream = FileInputStream(file)
        var uploaded = 0L
        fileImputStream.use { imputStream ->
            var readed: Int
            val handler = Handler(Looper.getMainLooper())


            while (imputStream.read(buffer).also {  readed = it } != -1 ){
                handler.post(ProgressUpdate(uploaded,lengthTotal))
                uploaded += readed
                sink.write(buffer, 0, readed)
            }

            //Forzar la Actualizacion coando llegue a 100%
            handler.post(ProgressUpdate(uploaded,lengthTotal))

        }
    }



    inner class ProgressUpdate(
        private val uploaded:Long,
        private val total: Long,
        ): Runnable{
        override fun run() {
            callback.onProggressUpdate(((100 * uploaded) / total ).toInt() )
        }
    }


    companion object{
        private const val DEFAULT_BUFFER_SIZE = 1024
    }



}