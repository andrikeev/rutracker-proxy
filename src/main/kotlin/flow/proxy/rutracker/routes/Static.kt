package flow.proxy.rutracker.routes

import io.ktor.server.application.*
import io.ktor.server.http.content.*
import io.ktor.server.routing.*

fun Application.configureStaticRoutes() {
    routing {
        static("/") {
            resources("static")
        }
    }
}
