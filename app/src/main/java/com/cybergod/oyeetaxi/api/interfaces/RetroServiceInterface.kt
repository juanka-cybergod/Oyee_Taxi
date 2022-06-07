package com.cybergod.oyeetaxi.api.interfaces


import com.cybergod.oyeetaxi.api.futures.configuration.model.Configuracion
import com.cybergod.oyeetaxi.api.futures.configuration.model.configuration.UpdateConfiguracion
import com.cybergod.oyeetaxi.api.futures.file.model.response.FicherosRespuesta
import com.cybergod.oyeetaxi.api.futures.file.model.types.TipoFichero
import com.cybergod.oyeetaxi.api.futures.province.model.Provincia
import com.cybergod.oyeetaxi.api.futures.travel.model.Viaje
import com.cybergod.oyeetaxi.api.futures.user.model.response.LoginRespuesta
import com.cybergod.oyeetaxi.api.futures.user.model.response.RequestVerificationCodeResponse
import com.cybergod.oyeetaxi.api.futures.vehicle.model.response.VehiculoResponse
import com.cybergod.oyeetaxi.api.futures.user.model.response.UsuariosPaginados
import com.cybergod.oyeetaxi.api.futures.user.model.Usuario
import com.cybergod.oyeetaxi.api.futures.user.model.requestFilter.UserFilterOptions
import com.cybergod.oyeetaxi.api.futures.vehicle.model.Vehiculo
import com.cybergod.oyeetaxi.api.futures.valoration.model.Valoracion
import com.cybergod.oyeetaxi.api.futures.vehicle_type.model.TipoVehiculo
import com.cybergod.oyeetaxi.utils.Constants.URL_BASE_CONFIGURACION
import com.cybergod.oyeetaxi.utils.Constants.URL_BASE_FICHEROS
import com.cybergod.oyeetaxi.utils.Constants.URL_BASE_PROVINCIAS
import com.cybergod.oyeetaxi.utils.Constants.URL_BASE_TIPO_VEHICULOS
import com.cybergod.oyeetaxi.utils.Constants.URL_BASE_USUARIOS
import com.cybergod.oyeetaxi.utils.Constants.URL_BASE_VALORACION
import com.cybergod.oyeetaxi.utils.Constants.URL_BASE_VEHICULOS
import com.cybergod.oyeetaxi.utils.Constants.URL_BASE_VIAJES
import com.cybergod.oyeetaxi.api.futures.configuration.model.SmsProvider
import com.cybergod.oyeetaxi.api.futures.share.model.Ubicacion
import com.cybergod.oyeetaxi.api.futures.vehicle.model.requestFilter.VehicleFilterOptions
import com.cybergod.oyeetaxi.api.futures.vehicle.model.response.VehiculosPaginados
import com.cybergod.oyeetaxi.utils.Constants.QUERRY_PAGE_SIZE
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*


interface RetroServiceInterface {

    /** USUARIOS ***********************************************************/
//    @GET(URL_BASE_USUARIOS + "getAllUsers")
//    suspend fun getAllUsers(): Response<ArrayList<Usuario>>

    @PUT(URL_BASE_USUARIOS + "searchUsersPaginatedWithFilter")
    suspend fun searchUsersPaginatedWithFilter(
        @Query("size") size: Int = QUERRY_PAGE_SIZE,
        @Query("page") page: Int = 0,
        @Query("sort") sort: String = "nombre",
        @Body userFilterOptions : UserFilterOptions?=UserFilterOptions(),
    ): Response<UsuariosPaginados>

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

    @GET(URL_BASE_USUARIOS + "requestOTPCodeToSMSTest") //requestOTPCodeToSMS **Usar en Release
    suspend fun requestOTPCodeToSMSTest(@Query("phoneNumber") userPhone: String): Response<String>

    @PUT(URL_BASE_USUARIOS + "updateUserLocationById")
    suspend fun updateUserLocationById(@Query("idUsuario") idUsuario: String,@Body ubicacion: Ubicacion) : Response<Boolean>

    @GET(URL_BASE_USUARIOS + "requestVerificationCodeToRestorePassword")
    suspend fun requestVerificationCodeToRestorePassword(@Query("emailOrPhone") emailOrPhone: String): Response<RequestVerificationCodeResponse>

    @GET(URL_BASE_USUARIOS + "verifyOTPCodeToRestorePassword")
    suspend fun verifyOTPCodeToRestorePassword(@Query("idUsuario") idUsuario: String,@Query("otpCode") otpCode: String): Response<Boolean>


    /** VEHICULOS ***********************************************************/

    @PUT(URL_BASE_VEHICULOS + "searchVehiclesPaginatedWithFilter")
    suspend fun searchVehiclesPaginatedWithFilter(
        @Query("size") size: Int = QUERRY_PAGE_SIZE,
        @Query("page") page: Int = 0,
        @Query("sort") sort: String = "fechaDeRegistro,DESC",
        @Body vehicleFilterOptions : VehicleFilterOptions?=VehicleFilterOptions(),
    ): Response<VehiculosPaginados>

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



    /** TIPO VEHICULOS ***********************************************************/
    @GET(URL_BASE_TIPO_VEHICULOS + "getAllVehiclesType")
    suspend fun getAllVehiclesType(): Response<ArrayList<TipoVehiculo>>

    @GET(URL_BASE_TIPO_VEHICULOS + "getAvailableVehiclesType")
    suspend fun getAvailableVehiclesType(): Response<ArrayList<TipoVehiculo>>

    @PUT(URL_BASE_TIPO_VEHICULOS + "updateVehicleType")
    suspend fun updateVehicleType(@Body tipoVehiculo: TipoVehiculo):Response<TipoVehiculo>


    /** PROVINCIAS ***********************************************************/
    @GET(URL_BASE_PROVINCIAS + "getAllProvinces")
    suspend fun getAllProvinces(): Response<ArrayList<Provincia>>

    @GET(URL_BASE_PROVINCIAS + "getAvailableProvinces")
    suspend fun getAvailableProvinces(): Response<ArrayList<Provincia>>

    @PUT(URL_BASE_PROVINCIAS + "updateProvince")
    suspend fun updateProvince(@Body provincia: Provincia):Response<Provincia>

    @POST(URL_BASE_PROVINCIAS + "addProvince")
    suspend fun addProvince(@Body provincia: Provincia):Response<Provincia>

    /** CONFIGURACION ***********************************************************/
    @GET(URL_BASE_CONFIGURACION + "isServerActive")
    suspend fun isServerActive(): Response<Boolean>

    @GET(URL_BASE_CONFIGURACION + "getSmsProvider")
    suspend fun getSmsProvider(): Response<SmsProvider>

    @GET(URL_BASE_CONFIGURACION + "getUpdateConfiguration")
    suspend fun getUpdateConfiguration(): Response<UpdateConfiguracion>

    @GET(URL_BASE_CONFIGURACION + "getConfiguration")
    suspend fun getConfiguration(): Response<Configuracion>

    @PUT(URL_BASE_CONFIGURACION + "updateConfiguration")
    suspend fun updateConfiguration(@Body configuracion : Configuracion): Response<Configuracion>


    /** UPPLOAD FILES ***********************************************************/

    @Multipart
    @POST(URL_BASE_FICHEROS + "uploadSingleFileByType")
    suspend fun uploadSingleFile(@Part file: MultipartBody.Part, @Part("id") id: String, @Part("fileType") fileType: TipoFichero) : Response<FicherosRespuesta>

    /** VIAJES ***********************************************************/
    @POST(URL_BASE_VIAJES + "addViaje")
    suspend fun addViaje(@Body viaje: Viaje): Response<Viaje>


    /** VALORACIONES ***********************************************************/

    @GET(URL_BASE_VALORACION + "getValorationByUsersId")
    suspend fun getValorationByUsersId(@Query("idUsuarioValora") idUsuarioValora: String, @Query("idUsuarioValorado") idUsuarioValorado: String): Response<Valoracion>

    @POST(URL_BASE_VALORACION + "addUpdateValoration")
    suspend fun addUpdateValoration(@Body valoracion: Valoracion): Response<Valoracion>



}