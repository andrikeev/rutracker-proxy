package flow.proxy.rutracker.routes

import flow.proxy.rutracker.di.inject
import flow.proxy.rutracker.domain.GetCategoryPageUseCase
import flow.proxy.rutracker.domain.GetForumUseCase
import io.ktor.server.application.*
import io.ktor.server.routing.*
import io.ktor.server.util.*

fun Application.configureForumRoutes() {
    val getForumUseCase by inject<GetForumUseCase>()
    val getCategoryPageUseCase by inject<GetCategoryPageUseCase>()

    routing {
        get("/forum") {
            respond(getForumUseCase())
        }

        get("/category") {
            val id: String by call.request.queryParameters
            val page: Int? = call.request.queryParameters["page"]?.toIntOrNull()
            check(id.isNotEmpty())
            check(page == null || page > 0)
            respond(getCategoryPageUseCase(id, page))
        }
    }
}
