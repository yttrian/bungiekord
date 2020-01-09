package org.yttr

import io.ktor.client.HttpClient
import io.ktor.client.engine.jetty.Jetty
import io.ktor.client.features.defaultRequest
import io.ktor.client.features.json.GsonSerializer
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.request.header
import io.ktor.client.request.host
import io.ktor.http.userAgent

class Bungiekord(configure: Config.() -> Unit = {}) {
    /**
     * Configure the Bungiekord client based on the described needed
     * information here: https://github.com/Bungie-net/api
     */
    class Config {
        /**
         * The API key issued by Bungie for use in all requests
         */
        var apiKey: String? = null
        var appName: String = "Unnamed Bungiekord Application"
        var version: String = "Unknown"
        var appId: Int = 0
        var webUrl: String = "Unknown"
        var contactEmail: String = "Unknown"
        /**
         * Set or get the User-Agent sent with all requests.
         * The format is: AppName/Version AppId/appIdNum (+webUrl;contactEmail)
         * Setting it yourself will attempt to extract the details in the shown format.
         */
        var userAgent: String
            get() = "$appName/$version AppId/$appId (+$webUrl;$contactEmail)"
            set(value) {
                val userAgentStringRegex = Regex("(.+)/(.+) AppId/(\\d+) \\(\\+(.+);(.+)\\)")
                userAgentStringRegex.matchEntire(value)?.groupValues?.let {
                    appName = it[1]
                    version = it[2]
                    appId = it[3].toInt()
                    webUrl = it[4]
                    contactEmail = it[5]
                } ?: throw IllegalArgumentException("Malformed user agent string!")
            }
    }

    val config: Config by lazy {
        val c = Config()
        c.configure()
        c
    }

    private val client = HttpClient(Jetty) {
        defaultRequest {
            host = "https://www.bungie.net/Platform"
            header("X-API-KEY", config.apiKey)
            userAgent(config.userAgent)
        }

        install(JsonFeature) {
            serializer = GsonSerializer()
        }
    }
}