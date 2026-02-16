package com.androbeat.androbeatagent.data.remote.rest.restApiClient

import android.content.Context
import com.androbeat.androbeatagent.BuildConfig
import com.androbeat.androbeatagent.utils.ApplicationStatus
import okhttp3.OkHttpClient
import java.io.InputStream
import java.security.KeyStore
import java.security.cert.CertificateFactory
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManagerFactory
import javax.net.ssl.X509TrustManager

object SelfSignedSslCertsUtils {

    fun getSelfSignedSslClient(context: Context, certificateRawResId: Int): OkHttpClient {
        try {
            val certificateFactory = CertificateFactory.getInstance("X.509")
            val certificate: InputStream = context.resources.openRawResource(certificateRawResId)

            val certificateInput = certificate.use {
                certificateFactory.generateCertificate(it)
            }

            val keyStoreType = KeyStore.getDefaultType()
            val keyStore = KeyStore.getInstance(keyStoreType).apply {
                load(null, null)
                setCertificateEntry("ca", certificateInput)
            }

            val trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm()).apply {
                init(keyStore)
            }

            val trustManagers = trustManagerFactory.trustManagers
            val trustManager = trustManagers[0] as X509TrustManager

            val sslContext = SSLContext.getInstance("TLSv1.2").apply {
                init(null, arrayOf(trustManager), null)
            }
            ApplicationStatus.postOK()

            val builder = OkHttpClient.Builder()
                .sslSocketFactory(sslContext.socketFactory, trustManager)

            if (BuildConfig.DEBUG) {
                builder.hostnameVerifier { _, _ -> true }
            }

            return builder.build()

        } catch (e: Exception) {
            //Logger.logError("SSL", "Error creating SSL client $e")
            ApplicationStatus.postError("Error creating SSL client: ${e.message}")
            throw RuntimeException("Failed to create SSL client $e")
        }
    }
}
