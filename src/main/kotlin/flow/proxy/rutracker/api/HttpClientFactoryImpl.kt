package flow.proxy.rutracker.api

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.logging.*

class HttpClientFactoryImpl : HttpClientFactory {

    private val httpClient: HttpClient by lazy {
        HttpClient(CIO) {
            defaultRequest { url("https://rutracker.org/forum/") }
            install(Logging)
        }
    }

    override fun create() = httpClient
}
