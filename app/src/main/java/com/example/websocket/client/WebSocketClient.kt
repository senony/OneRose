package com.example.websocket.client

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.util.Log
import com.example.websocket.WebSocketListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket
import java.security.SecureRandom
import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.net.ssl.HostnameVerifier
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLSocketFactory
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

class WebSocketClient(private val direction: String) {

    private var webSocket: WebSocket? = null

    init {
        connectWebSocket()
    }

    private fun getUnsafeOkHttpClient(): OkHttpClient? {
        var okHttpClient: OkHttpClient? = null
        okHttpClient = try {
            val trustAllCerts = arrayOf<TrustManager>(
                @SuppressLint("CustomX509TrustManager")
                object : X509TrustManager {
                    @SuppressLint("TrustAllX509TrustManager")
                    @Throws(CertificateException::class)
                    override fun checkClientTrusted(
                        chain: Array<X509Certificate?>?,
                        authType: String?
                    ) {
                    }

                    @SuppressLint("TrustAllX509TrustManager")
                    @Throws(CertificateException::class)
                    override fun checkServerTrusted(
                        chain: Array<X509Certificate?>?,
                        authType: String?
                    ) {
                    }

                    override fun getAcceptedIssuers(): Array<X509Certificate?> {
                        return arrayOfNulls<X509Certificate>(0)
                    }
                }
            )
            val sslContext = SSLContext.getInstance("SSL")
            sslContext.init(null, trustAllCerts, SecureRandom())
            val sslSocketFactory: SSLSocketFactory = sslContext.socketFactory
            OkHttpClient.Builder()
                .sslSocketFactory(sslSocketFactory, trustAllCerts[0] as X509TrustManager)
                .hostnameVerifier(HostnameVerifier { hostname, session -> true })
                .pingInterval(
                    0,
                    TimeUnit.SECONDS
                ) //                    .addInterceptor(new WebSocketHeaderInterceptor(_accessToken))
                .build()
        } catch (e: java.lang.Exception) {
            throw RuntimeException(e)
        }
        return okHttpClient
    }

    private fun connectWebSocket() {
        try {
            val url = "wss://cobot.center:8287/pang/ws/sub?channel=cjdj5p1c000rn202b9e0&track=colink&mode=bundle"
            val client: OkHttpClient = getUnsafeOkHttpClient()!!
            val listener = WebSocketListener(direction)
            Log.d(TAG, "-----Websocket url:${url}")
            val request: Request = Request.Builder().url(url).build()
            webSocket = client.newWebSocket(request, listener)
            Log.d(TAG, "WebSocket connection successful")
            client.dispatcher.executorService.shutdown()
        } catch (e: Exception) {
            Log.e(TAG, "WebSocket exception: $e")
        }
    }

    fun isWebSocketConnected(): Boolean {
        return webSocket?.send("Ping") ?: false
    }

    // Close WebSocket connection
    fun closeWebSocket() {
        webSocket?.close(1000, "User requested closing.")
    }

    companion object {
        private const val RECONNECT_INTERVAL_MILLIS = 10000L // 5 seconds
    }
}