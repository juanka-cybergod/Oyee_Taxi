package com.cybergod.oyeetaxi.di.utils

import android.annotation.SuppressLint
import okhttp3.OkHttpClient
import java.security.SecureRandom
import java.security.cert.X509Certificate
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager


import javax.net.ssl.HostnameVerifier

object OkHttpClients {

    fun OkHttpClientNoSSLErrors(): OkHttpClient.Builder {
        val naiveTrustManager = @SuppressLint("CustomX509TrustManager")
        object : X509TrustManager {
            override fun getAcceptedIssuers(): Array<X509Certificate> = arrayOf()
            override fun checkClientTrusted(certs: Array<X509Certificate>, authType: String) = Unit
            override fun checkServerTrusted(certs: Array<X509Certificate>, authType: String) = Unit
        }

        val insecureSocketFactory = SSLContext.getInstance("TLSv1.2").apply {
            val trustAllCerts = arrayOf<TrustManager>(naiveTrustManager)
            init(null, trustAllCerts, SecureRandom())
        }.socketFactory

        val okHttpClient = OkHttpClient.Builder()
        okHttpClient.sslSocketFactory(insecureSocketFactory, naiveTrustManager)
        okHttpClient.hostnameVerifier(HostnameVerifier { _, _ -> true })
        return okHttpClient
    }

    fun OkHttpClientDefault(): OkHttpClient.Builder{
        return OkHttpClient.Builder()
    }


}