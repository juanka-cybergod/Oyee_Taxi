package com.cybergod.oyeetaxi.api.futures.file.repositories


import android.util.Log
import com.cybergod.oyeetaxi.api.interfaces.RetroServiceInterface
import com.cybergod.oyeetaxi.api.futures.file.model.types.TipoFichero
import com.cybergod.oyeetaxi.api.futures.file.request_body.UploadRequestBody
import com.cybergod.oyeetaxi.api.utils.UtilsApi.handleRequest
import com.cybergod.oyeetaxi.api.utils.UtilsApi.logResponse
import com.cybergod.oyeetaxi.utils.Constants.UNKNOWN_CLASS
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import java.io.File
import javax.inject.Inject

class FilesRepository @Inject constructor(private val retroServiceInterface: RetroServiceInterface){

    private val className = this.javaClass.simpleName ?: UNKNOWN_CLASS



    suspend fun uploadSingleFileByType(file: File?, context: UploadRequestBody.UploadCallback, id:String, tipoFichero: TipoFichero):String?   {

        if (file != null) {
            val body = UploadRequestBody(file,"image/*".toMediaType(),context)
            val multiPartFile = MultipartBody.Part.createFormData("file",file.name,body)

            handleRequest {
                retroServiceInterface.uploadSingleFileByType(multiPartFile,id,tipoFichero)
            }?.let { response ->

                return if (response.isSuccessful) {

                    logResponse(
                        className = className,
                        metodo = object {}.javaClass.enclosingMethod!!,
                        responseCode = response.code(),
                        responseHeaders = response.headers().toString(),
                        responseBody = response.body().toString()
                    )

                    val urlRelativa :String? = response.body()?.url
                    Log.d(className, "uploadSingleFile: R_Body: urlRelativa : $urlRelativa")
                    urlRelativa
                } else null

            }

            return null



        } else {
            return null
        }



    }


    suspend fun uploadAppUpdateFile(file: File?, context: UploadRequestBody.UploadCallback, fileName:String):String?   {

        if (file != null) {

            val body = UploadRequestBody(file,MultipartBody.FORM,context)
            val multiPartFile = MultipartBody.Part.createFormData("file",fileName,body)

            handleRequest {
                retroServiceInterface.uploadSingleFile(multiPartFile,fileName)
            }?.let { response ->

                return if (response.isSuccessful) {

                    logResponse(
                        className = className,
                        metodo = object {}.javaClass.enclosingMethod!!,
                        responseCode = response.code(),
                        responseHeaders = response.headers().toString(),
                        responseBody = response.body().toString()
                    )

                    val urlRelativa :String? = response.body()?.url
                    Log.d(className, "uploadSingleFile: R_Body: urlRelativa : $urlRelativa")
                    urlRelativa
                } else null

            }

            return null

        } else {
            return null
        }


    }


    /** OTRO METODO PARA SUBIR FICHEROS CON OKHTTP *Funciona solo hay q corregirlo
    //            val fileBody = RequestBody.create(MultipartBody.FORM,file)
    //
    //            val body : RequestBody = MultipartBody.Builder()
    //                .setType(MultipartBody.FORM)
    //                .addFormDataPart("type","apk")
    //                .addFormDataPart("file",fileName,fileBody)
    //                .build()
    //
    //
    //
    //            val request = Request.Builder()
    //                .addHeader(Constants.Authorization, UtilsGlobal.getOyeeTaxiApiKeyEncoded())
    //                .url(Constants.URL_BASE)
    //                .post(body)
    //                .build()
    //
    //            val okHttpClient = OkHttpClientNoSSLErrors().build()
    //
    //            try {
    //                val response = okHttpClient.newCall(request).execute()
    //                if (response.isSuccessful) {
    //                    println("Call OK")
    //                } else {
    //                    println("Call Error")
    //                }
    //            } catch (e:Exception) {
    //                println("Call Exception $e")
    //            }
     */



}







/*
    //uploadSingleFile
    fun uploadSingleFile(file: File?, URL: MutableLiveData<String>, context: UploadRequestBody.UploadCallback, optionalName:String, created :MutableLiveData<Boolean>)   {

        if (file != null) {


            val body = UploadRequestBody(file,"image",context)
            val multiPartFile = MultipartBody.Part.createFormData("file",file.name,body)


            val fileName = if (optionalName.trim().isEmpty()) {
                getRamdomUUID()
            } else {
                optionalName+"."+file.extension
            }




            val call : Call<FicherosRespuesta>  = retroServiceInterface.uploadSingleFile(multiPartFile,fileName)


            call.enqueue(object : Callback<FicherosRespuesta>{
                override fun onResponse(call: Call<FicherosRespuesta>, response: Response<FicherosRespuesta>) {

                    //Log.d(TAG_CLASS_NAME, "uploadSingleFile Codigo : ${response.code()}")
                    //Log.d(TAG_CLASS_NAME, "uploadSingleFile Header : ${response.headers()}")
                    Log.d(TAG_CLASS_NAME, "uploadSingleFile Body : ${response.body()}")


                    if (response.isSuccessful) {
                        //Verifica que el codigo de Respuesta sea Correcto
                        if (response.code() == RESPONSE_CODE_OK) {

                            val urlRelativa :String? = response.body()?.url
                            if (!urlRelativa.isNullOrEmpty()) {

                                Log.d(TAG_CLASS_NAME, "urlRelativa : $urlRelativa")
                                URL.postValue(urlRelativa)
                                created.postValue(true)
                            } else {
                                URL.postValue(null)
                                created.postValue(false)
                            }



                        } else {
                            URL.postValue(null)
                            created.postValue(false)
                        }
                    } else {
                        URL.postValue(null)
                        created.postValue(false)
                    }

                }

                override fun onFailure(call: Call<FicherosRespuesta>, t: Throwable) {
                    Log.d(TAG_CLASS_NAME , t.message!!)
                    URL.postValue(null)
                    created.postValue(false)
                }
            })


        } else {
            URL.postValue(null)
            created.postValue(false)
        }



    }


 */