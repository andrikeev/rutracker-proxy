package flow.proxy.rutracker.routes

import flow.proxy.rutracker.di.inject
import flow.proxy.rutracker.domain.GetIndexUseCase
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureMainRoutes() {
    val getIndexUseCase by inject<GetIndexUseCase>()

    routing {
        get("/") {
            val token = call.request.authToken
            respond(getIndexUseCase(token))
        }
        get("/index") {
            val token = call.request.authToken
            respond(getIndexUseCase(token))
        }
    }
}
