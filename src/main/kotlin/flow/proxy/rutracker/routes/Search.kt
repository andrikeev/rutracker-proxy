package flow.proxy.rutracker.routes

import flow.proxy.rutracker.di.inject
import flow.proxy.rutracker.domain.GetSearchPageUseCase
import flow.proxy.rutracker.models.search.SearchPeriod
import flow.proxy.rutracker.models.search.SearchSortOrder
import flow.proxy.rutracker.models.search.SearchSortType
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureSearchRoutes() {
    val getSearchPageUseCase by inject<GetSearchPageUseCase>()

    routing {
        get("/search") {
            val token = call.request.authToken
            val query = call.request.queryParameters["query"]
            val categories = call.request.queryParameters["categories"]
            val author = call.request.queryParameters["author"]
            val authorId = call.request.queryParameters["authorId"]
            val sort = call.request.queryParameters["sort"]?.toEnumOrNull<SearchSortType>()
            val order = call.request.queryParameters["order"]?.toEnumOrNull<SearchSortOrder>()
            val period = call.request.queryParameters["period"]?.toEnumOrNull<SearchPeriod>()
            val page = call.request.queryParameters["page"]?.toIntOrNull()
            respond(
                getSearchPageUseCase(
                    token, query, categories, author,
                    authorId, sort, order, period, page,
                )
            )
        }
    }
}
