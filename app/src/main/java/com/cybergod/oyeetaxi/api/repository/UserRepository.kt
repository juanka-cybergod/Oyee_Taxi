package com.cybergod.oyeetaxi.api.repository


import androidx.lifecycle.MutableLiveData
import com.cybergod.oyeetaxi.api.interfaces.RetroServiceInterface
import com.cybergod.oyeetaxi.api.model.Ubicacion
import com.cybergod.oyeetaxi.api.model.Usuario
import com.cybergod.oyeetaxi.api.model.response.LoginRespuesta
import com.cybergod.oyeetaxi.api.model.response.RequestVerificationCodeResponse
import com.cybergod.oyeetaxi.api.utils.UtilsApi.handleRequest
import com.cybergod.oyeetaxi.api.utils.UtilsApi.logResponse
import com.cybergod.oyeetaxi.utils.Constants
import com.cybergod.oyeetaxi.utils.Constants.RESPONSE_CODE_OK
import com.cybergod.oyeetaxi.utils.UtilsGlobal.passwordEncode
import javax.inject.Inject


class UserRepository @Inject constructor(private val retroServiceInterface: RetroServiceInterface) {

    private val className = this.javaClass.simpleName?:"ClaseDesconocida"

    suspend fun getAllUsers() : List<Usuario>?  {

        handleRequest {
            retroServiceInterface.getAllUsers()
        }?.let { response ->

            return if (response.isSuccessful) {

                logResponse(
                    className = className,
                    metodo = object{}.javaClass.enclosingMethod!!,
                    responseCode = response.code(),
                    responseHeaders = response.headers().toString(),
                    responseBody = response.body().toString()
                )

                if (response.code() == RESPONSE_CODE_OK) {
                    response.body()?.toList()
                } else  null
            } else null

        }

        return null
    }


    suspend fun getUserById(userId: String) : Usuario?  {

        handleRequest {
            retroServiceInterface.getUserById(userId)
        }?.let { response ->

            return if (response.isSuccessful) {

                logResponse(
                    className = className,
                    metodo = object{}.javaClass.enclosingMethod!!,
                    responseCode = response.code(),
                    responseHeaders = response.headers().toString(),
                    responseBody = response.body().toString()
                )

                if (response.code() == RESPONSE_CODE_OK) {
                    response.body()
                } else  null
            } else null

        }

        return null
    }


    suspend fun addUser(usuario: Usuario) : Boolean?  {

        usuario.contrasena = passwordEncode(usuario.contrasena!!)


        handleRequest {
            retroServiceInterface.addUser(usuario)
        }?.let { response ->

            return if (response.isSuccessful) {

                logResponse(
                    className = className,
                    metodo = object{}.javaClass.enclosingMethod!!,
                    responseCode = response.code(),
                    responseHeaders = response.headers().toString(),
                    responseBody = response.body().toString()
                )

                if (response.code() == RESPONSE_CODE_OK) {
                    response.body()
                    true
                } else false
            } else false

        }

        return null

    }


    suspend fun updateUser(user: Usuario, userResponse: MutableLiveData<Usuario>):Boolean?   {

        user.contrasena?.let { newPassword ->
            user.contrasena = passwordEncode(newPassword)
        }


        handleRequest {
            retroServiceInterface.updateUser(user)
        }?.let { response ->

            return if (response.isSuccessful) {

                logResponse(
                    className = className,
                    metodo = object{}.javaClass.enclosingMethod!!,
                    responseCode = response.code(),
                    responseHeaders = response.headers().toString(),
                    responseBody = response.body().toString()
                )

                if (response.code() == RESPONSE_CODE_OK) {
                    userResponse.postValue(
                        response.body()
                    )

                    true

                } else false
            } else false

        }

        return null


    }


    suspend fun userExistByPhone(userPhone: String):Boolean?   {

        handleRequest {
            retroServiceInterface.userExistByPhone(userPhone)
        }?.let { response ->

            return if (response.isSuccessful) {

                logResponse(
                    className = className,
                    metodo = object{}.javaClass.enclosingMethod!!,
                    responseCode = response.code(),
                    responseHeaders = response.headers().toString(),
                    responseBody = response.body().toString()
                )

                if (response.code() == RESPONSE_CODE_OK) {
                    response.body()
                } else false
            } else false

        }

        return null

    }


    suspend fun loginUser(userPhone: String,password: String): LoginRespuesta?   {

        handleRequest {
            retroServiceInterface.loginUser(userPhone,passwordEncode(password))
        }?.let { response ->

            return if (response.isSuccessful) {

                logResponse(
                    className = className,
                    metodo = object{}.javaClass.enclosingMethod!!,
                    responseCode = response.code(),
                    responseHeaders = response.headers().toString(),
                    responseBody = response.body().toString()
                )

                if (response.code() == RESPONSE_CODE_OK) {
                    response.body()
                } else null
            } else null

        }

        return null
    }


    suspend fun updateUserLocationById(userId: String, ubicacion: Ubicacion):Boolean?   {

        handleRequest {
            retroServiceInterface.updateUserLocationById(userId,ubicacion)
        }?.let { response ->

            return if (response.isSuccessful) {

                logResponse(
                    className = className,
                    metodo = object{}.javaClass.enclosingMethod!!,
                    responseCode = response.code(),
                    responseHeaders = response.headers().toString(),
                    responseBody = response.body().toString()
                )

                if (response.code() == RESPONSE_CODE_OK) {
                    response.body()
                } else null
            } else null

        }

        return null



    }


    suspend fun requestOTPCodeToSMS(userPhone: String):String?   {

        handleRequest {
            retroServiceInterface.requestOTPCodeToSMSTest(userPhone)
        }?.let { response ->

            return if (response.isSuccessful) {

                logResponse(
                    className = className,
                    metodo = object {}.javaClass.enclosingMethod!!,
                    responseCode = response.code(),
                    responseHeaders = response.headers().toString(),
                    responseBody = response.body().toString()
                )

                if (response.code() == Constants.RESPONSE_CODE_OK && !response.body().isNullOrEmpty()) {
                    response.body()
                } else  null

            } else null

        }


        return null

    }


    suspend fun requestVerificationCode(userEmailOrPhone: String) :RequestVerificationCodeResponse?  {

        handleRequest {
            retroServiceInterface.requestVerificationCodeToRestorePassword(userEmailOrPhone)
        }?.let { response ->

            return if (response.isSuccessful) {

                logResponse(
                    className = className,
                    metodo = object{}.javaClass.enclosingMethod!!,
                    responseCode = response.code(),
                    responseHeaders = response.headers().toString(),
                    responseBody = response.body().toString()
                )

                if (response.code() == RESPONSE_CODE_OK) {
                    response.body()
                } else null
            } else null

        }

        return null

    }


    suspend fun verifyOTPCodeToRestorePassword(idUsuario: String,otpCode: String):Boolean?   {


        handleRequest {
            retroServiceInterface.verifyOTPCodeToRestorePassword(idUsuario,otpCode)
        }?.let { response ->

            return if (response.isSuccessful) {

                logResponse(
                    className = className,
                    metodo = object{}.javaClass.enclosingMethod!!,
                    responseCode = response.code(),
                    responseHeaders = response.headers().toString(),
                    responseBody = response.body().toString()
                )

                if (response.code() == RESPONSE_CODE_OK) {
                    response.body()
                } else null
            } else null

        }

        return null
    }


}



























/* OTRA MANERA CON TRY CATCH SIN MANEJAR LAS RESPUESTAS
** SE PUEDE SUSAR RESULT SIMPLE DOC EN https://stackoverflow.com/questions/57323111/kotlin-android-retrofit-2-6-0-with-coroutines-error-handling
https://levelup.gitconnected.com/coroutines-retrofit-and-a-nice-way-to-handle-responses-769e013ee6ef
* https://stackoverflow.com/questions/58083309/coroutines-and-retrofit-best-way-to-handle-errors
* https://stackoverflow.com/questions/67812248/elegant-way-of-handling-error-using-retrofit-kotlin-flow
* https://stackoverflow.com/questions/66256991/kotlin-handle-retrofit-request-with-coroutines
* https://exchangetuts.com/kotlin-android-retrofit-260-with-coroutines-error-handling-1639763122988424
//        val response = try {
//            retroServiceInterface.loginUser(userPhone,passwordEncode(password))
//        } catch (e: Exception) {
//            Log.d(className, "Exception : $e")
//            return null
//        }


//        return if (response.isSuccessful) {
//
//            logResponse(
//                className = className,
//                metodo = object{}.javaClass.enclosingMethod!!,
//                responseCode = response.code(),
//                responseHeaders = response.headers().toString(),
//                responseBody = response.body().toString()
//            )
//
//            if (response.code() == RESPONSE_CODE_OK) {
//                response.body()
//            } else null
//        } else null
 */