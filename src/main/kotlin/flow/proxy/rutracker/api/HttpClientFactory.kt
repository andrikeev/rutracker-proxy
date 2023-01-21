package flow.proxy.rutracker.api

import io.ktor.client.*

interface HttpClientFactory {

    fun create(): HttpClient
}
