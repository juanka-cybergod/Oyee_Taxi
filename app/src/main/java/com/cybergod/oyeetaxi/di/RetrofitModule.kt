package com.cybergod.oyeetaxi.di


import com.cybergod.oyeetaxi.api.interfaces.RetroServiceInterface
import com.cybergod.oyeetaxi.di.utils.OkHttpClients
import com.cybergod.oyeetaxi.utils.Constants
import com.cybergod.oyeetaxi.utils.Constants.Authorization
import com.cybergod.oyeetaxi.utils.Constants.URL_BASE
import com.cybergod.oyeetaxi.utils.UtilsGlobal.getOyeeTaxiApiKeyEncoded
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object RetrofitModule {


    @Singleton
    @Provides
    fun getRetrofitInstance() : Retrofit {

        val logging = HttpLoggingInterceptor().apply {
            setLevel(HttpLoggingInterceptor.Level.BODY)
        }


        val headerInterceptor = Interceptor { chain ->
            var request: Request = chain.request()

            request = request.newBuilder()
                .addHeader(Authorization, getOyeeTaxiApiKeyEncoded())
                .build()

            chain.proceed(request)
        }


        val client = OkHttpClients.OkHttpClientNoSSLErrors() // or OkHttpClient.Builder()
            .addInterceptor(headerInterceptor)
            .addInterceptor(logging)
            .readTimeout(1,TimeUnit.MINUTES)
            .build()
//        val client = when {
//            URL_USE_SSL -> {OkHttpClientNoSSLErrors()}
//            else ->{OkHttpClientDefault()}
//        }
//        client






        return Retrofit.Builder()
            .baseUrl(URL_BASE)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }


    @Singleton
    @Provides
    fun getRetroServiceInterface(retrofit: Retrofit): RetroServiceInterface {
        return retrofit.create(RetroServiceInterface::class.java)
    }



}




