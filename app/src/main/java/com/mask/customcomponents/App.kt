package com.mask.customcomponents

import android.app.Application
import com.mask.customcomponents.config.Global
import com.mask.customcomponents.utils.LogUtil
import java.security.SecureRandom
import java.security.cert.X509Certificate
import javax.net.ssl.HttpsURLConnection
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager


/**
 * Application
 *
 * Create by lishilin on 2025/2/18
 */
class App : Application() {

    companion object {
        lateinit var context: App
            private set
    }

    override fun onCreate() {
        super.onCreate()
        context = this

        LogUtil.i("${Global.Tag.APP} onCreate $this")

        handleSSLHandshake()
    }

    /**
     * 忽略 Https 证书校验
     */
    private fun handleSSLHandshake() {
        try {
            val trustAllCerts = arrayOf<TrustManager>(object : X509TrustManager {
                override fun getAcceptedIssuers(): Array<X509Certificate> {
                    return emptyArray()
                }

                override fun checkClientTrusted(certs: Array<X509Certificate>, authType: String) {
                }

                override fun checkServerTrusted(certs: Array<X509Certificate>, authType: String) {
                }
            })

            val sc = SSLContext.getInstance("TLS")
            // trustAllCerts信任所有的证书
            sc.init(null, trustAllCerts, SecureRandom())
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.socketFactory)
            HttpsURLConnection.setDefaultHostnameVerifier { hostname, session -> true }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}