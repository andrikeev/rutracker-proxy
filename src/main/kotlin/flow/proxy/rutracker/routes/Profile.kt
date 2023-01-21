package flow.proxy.rutracker.routes

import flow.proxy.rutracker.di.inject
import flow.proxy.rutracker.domain.GetProfileUseCase
import flow.proxy.rutracker.models.toResult
import io.ktor.server.application.*
import io.ktor.server.routing.*
import io.ktor.server.util.*

fun Application.configureProfileRoutes() {
    val getProfileUseCase by inject<GetProfileUseCase>()

    routing {
        get("/profile") {
            val userId: String by call.request.queryParameters
            check(userId.isNotEmpty())
            respond(getProfileUseCase(userId).toResult())
        }
    }
}
