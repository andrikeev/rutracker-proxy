package flow.proxy.rutracker.routes

import io.ktor.server.application.*
import io.ktor.server.routing.*
import io.ktor.server.util.*
import flow.proxy.rutracker.di.inject
import flow.proxy.rutracker.domain.AddFavoriteUseCase
import flow.proxy.rutracker.domain.GetFavoritesUseCase
import flow.proxy.rutracker.domain.RemoveFavoriteUseCase

fun Application.configureFavoritesRoutes() {
    val getFavoritesUseCase by inject<GetFavoritesUseCase>()
    val addFavoriteUseCase by inject<AddFavoriteUseCase>()
    val removeFavoriteUseCase by inject<RemoveFavoriteUseCase>()

    routing {
        get("/favorites") {
            respond(getFavoritesUseCase(call.request.authToken))
        }

        post("/favorites/add") {
            val token = call.request.authToken
            val id: String by call.request.queryParameters
            check(id.isNotEmpty())
            respond(addFavoriteUseCase(token, id))
        }

        post("/favorites/remove") {
            val token = call.request.authToken
            val id: String by call.request.queryParameters
            check(id.isNotEmpty())
            respond(removeFavoriteUseCase(token, id))
        }
    }
}
