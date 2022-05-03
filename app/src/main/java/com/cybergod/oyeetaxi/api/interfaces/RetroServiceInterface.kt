package com.cybergod.oyeetaxi.api.interfaces


import com.cybergod.oyeetaxi.api.model.*
import com.cybergod.oyeetaxi.api.model.configuration.UpdateConfiguracion
import com.cybergod.oyeetaxi.api.model.response.FicherosRespuesta
import com.cybergod.oyeetaxi.api.model.response.LoginRespuesta
import com.cybergod.oyeetaxi.api.model.response.RequestVerificationCodeResponse
import com.cybergod.oyeetaxi.api.model.response.VehiculoResponse
import com.cybergod.oyeetaxi.utils.Constants.URL_BASE_CONFIGURACION
import com.cybergod.oyeetaxi.utils.Constants.URL_BASE_FICHEROS
import com.cybergod.oyeetaxi.utils.Constants.URL_BASE_PROVINCIAS
import com.cybergod.oyeetaxi.utils.Constants.URL_BASE_SMS
import com.cybergod.oyeetaxi.utils.Constants.URL_BASE_TIPO_VEHICULOS
import com.cybergod.oyeetaxi.utils.Constants.URL_BASE_USUARIOS
import com.cybergod.oyeetaxi.utils.Constants.URL_BASE_VALORACION
import com.cybergod.oyeetaxi.utils.Constants.URL_BASE_VEHICULOS
import com.cybergod.oyeetaxi.utils.Constants.URL_BASE_VIAJES
import com.oyeetaxi.cybergod.Modelos.SmsProvider
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*


interface RetroServiceInterface {

    //TODO USUARIOS ***********************************************************

    @GET(URL_BASE_USUARIOS + "getUserById={id}")
    suspend fun getUserById( @Path("id") id : String ): Response<Usuario>

    @POST(URL_BASE_USUARIOS + "addUser")
    suspend fun addUser(@Body user : Usuario) : Response<Usuario>

    @PUT(URL_BASE_USUARIOS + "updateUser")
    suspend fun updateUser(@Body user : Usuario) : Response<Usuario>

    @GET(URL_BASE_USUARIOS + "loginUser")
    suspend fun loginUser(@Query("userPhone") userPhone: String, @Query("password") password: String): Response<LoginRespuesta>

    @GET(URL_BASE_USUARIOS + "userExistByPhone")
    suspend fun userExistByPhone(@Query("userPhone") userPhone: String): Response<Boolean>

    @PUT(URL_BASE_USUARIOS + "updateUserLocationById")
    suspend fun updateUserLocationById(@Query("idUsuario") idUsuario: String,@Body ubicacion: Ubicacion) : Response<Boolean>

    @GET(URL_BASE_USUARIOS + "requestVerificationCodeToRestorePassword")
    suspend fun requestVerificationCodeToRestorePassword(@Query("emailOrPhone") emailOrPhone: String): Response<RequestVerificationCodeResponse>

    @GET(URL_BASE_USUARIOS + "verifyOTPCodeToRestorePassword")
    suspend fun verifyOTPCodeToRestorePassword(@Query("idUsuario") idUsuario: String,@Query("otpCode") otpCode: String): Response<Boolean>


    //TODO VEHICULOS ***********************************************************
//    @GET(URL_BASE_VEHICULOS + "getAllVehicles")
//    fun getAllVehicles(): Call<ArrayList<Vehiculo>>
//
//    @GET(URL_BASE_VEHICULOS + "getVehicleById={id}")
//    fun getVehicleById( @Path("id") id : String ): Call<Vehiculo>

    @GET(URL_BASE_VEHICULOS + "getAllVehiclesFromUserId={id}")
    suspend fun getAllVehiclesFromUserId( @Path("id") id : String ): Response<ArrayList<VehiculoResponse>>

    @GET(URL_BASE_VEHICULOS + "getAvailableVehicles")
    suspend fun getAvailableVehicles(): Response<ArrayList<VehiculoResponse>>


    @GET(URL_BASE_VEHICULOS + "getActiveVehicleByUserId={id}")
    suspend fun getActiveVehicleByUserId( @Path("id") id : String ): Response<VehiculoResponse>

    @POST(URL_BASE_VEHICULOS + "addVehicle")
    suspend fun addVehicle(@Body vehiculo: Vehiculo): Response<Vehiculo>

    @PUT(URL_BASE_VEHICULOS + "updateVehicle")
    suspend fun updateVehicle(@Body vehiculo : Vehiculo) : Response<Vehiculo>

    @GET(URL_BASE_VEHICULOS + "setActiveVehicleToUserId")
    suspend fun setActiveVehicleToUserId(@Query("idUsuario") idUsuario: String, @Query("idVehiculo") idVehiculo: String): Response<Boolean>



    //TODO TIPO VEHICULOS ***********************************************************
    @GET(URL_BASE_TIPO_VEHICULOS + "getAllVehiclesType")
    suspend fun getAllVehiclesType(): Response<ArrayList<TipoVehiculo>>


    //TODO PROVINCIAS ***********************************************************
    @GET(URL_BASE_PROVINCIAS + "getAllProvinces")
    suspend fun getAllProvinces(): Response<ArrayList<Provincia>>


    //TODO CONFIGURACION ***********************************************************
    @GET(URL_BASE_CONFIGURACION + "isServerActive")
    suspend fun isServerActive(): Response<Boolean>

    @GET(URL_BASE_CONFIGURACION + "getSmsProvider")
    suspend fun getSmsProvider(): Response<SmsProvider>

    @GET(URL_BASE_CONFIGURACION + "getUpdateConfiguration")
    suspend fun getUpdateConfiguration(): Response<UpdateConfiguracion>

    @GET(URL_BASE_CONFIGURACION + "getConfiguration")
    suspend fun getConfiguration(): Response<Configuracion>

    @PUT(URL_BASE_CONFIGURACION + "updateConfiguration")
    suspend fun updateConfiguration(@Body configuracion :Configuracion): Response<Configuracion>


    //TODO TWILIO SMS ***************************************************************
    @GET(URL_BASE_SMS + "sendSMSTest")
    suspend fun sendSMSTest(@Query("phoneNumber") userPhone: String): Response<String>


    //TODO UPLOAD IMAGENES *********************************************************

    @Multipart
    @POST(URL_BASE_FICHEROS + "uploadSingleFileByType")
    suspend fun uploadSingleFile(@Part file: MultipartBody.Part, @Part("id") id: String, @Part("fileType") fileType: TipoFichero) : Response<FicherosRespuesta>

    //TODO VIAJES  ***************************************************************
    @POST(URL_BASE_VIAJES + "addViaje")
    suspend fun addViaje(@Body viaje: Viaje): Response<Viaje>


    // TODO VALORACIONES *********************************************************

    @GET(URL_BASE_VALORACION + "getValorationByUsersId")
    suspend fun getValorationByUsersId(@Query("idUsuarioValora") idUsuarioValora: String, @Query("idUsuarioValorado") idUsuarioValorado: String): Response<Valoracion>

    @POST(URL_BASE_VALORACION + "addUpdateValoration")
    suspend fun addUpdateValoration(@Body valoracion: Valoracion): Response<Valoracion>



}