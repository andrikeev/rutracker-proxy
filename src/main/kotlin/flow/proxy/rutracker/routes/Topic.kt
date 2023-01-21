package flow.proxy.rutracker.routes

import flow.proxy.rutracker.di.inject
import flow.proxy.rutracker.domain.AddCommentUseCase
import flow.proxy.rutracker.domain.GetCommentsPageUseCase
import flow.proxy.rutracker.domain.GetTopicUseCase
import io.ktor.server.application.*
import io.ktor.server.routing.*
import io.ktor.server.util.*

fun Application.configureTopicRoutes() {
    val getTopicUseCase by inject<GetTopicUseCase>()
    val getCommentsPageUseCase by inject<GetCommentsPageUseCase>()
    val addCommentUseCase by inject<AddCommentUseCase>()

    routing {
        get("/topic") {
            val token = call.request.authToken
            val id = call.request.queryParameters["id"]
            val pid = call.request.queryParameters["pid"]
            val page = call.request.queryParameters["page"]?.toIntOrNull()
            check(id != null || pid != null)
            respond(getTopicUseCase(token, id, pid, page))
        }

        get("/comments") {
            val token = call.request.authToken
            val id = call.request.queryParameters["id"]
            val pid = call.request.queryParameters["pid"]
            val page = call.request.queryParameters["page"]?.toIntOrNull()
            check(id != null || pid != null)
            respond(getCommentsPageUseCase(token, id, pid, page))
        }

        post("/comments/add") {
            val token = call.request.authToken
            val topicId: String by call.request.queryParameters
            val message: String by call.request.queryParameters
            check(topicId.isNotEmpty())
            check(message.isNotEmpty())
            respond(addCommentUseCase(token, topicId, message))
        }
    }
}
