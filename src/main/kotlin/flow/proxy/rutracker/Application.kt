package flow.proxy.rutracker

import io.ktor.server.engine.*
import io.ktor.server.netty.*
import flow.proxy.rutracker.plugins.*
import flow.proxy.rutracker.routes.*

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0") {
        configureKoin()
        configureMonitoring()
        configureSerialization()
        configureStatusPages()
        configureMainRoutes()
        configureAuthRoutes()
        configureForumRoutes()
        configureSearchRoutes()
        configureTopicRoutes()
        configureTorrentRoutes()
        configureFavoritesRoutes()
        configureProfileRoutes()
        configureStaticRoutes()
    }.start(wait = true)
}
