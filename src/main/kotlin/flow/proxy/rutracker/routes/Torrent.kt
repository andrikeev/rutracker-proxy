package flow.proxy.rutracker.routes

import flow.proxy.rutracker.di.inject
import flow.proxy.rutracker.domain.GetTorrentFileUseCase
import flow.proxy.rutracker.domain.GetTorrentUseCase
import flow.proxy.rutracker.models.Result
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.util.*

fun Application.configureTorrentRoutes() {
    val getTorrentUseCase by inject<GetTorrentUseCase>()
    val getTorrentFileUseCase by inject<GetTorrentFileUseCase>()

    routing {
        get("/torrent") {
            val token = call.request.authToken
            val id: String by call.request.queryParameters
            respond(getTorrentUseCase(token, id))
        }

        get("/download") {
            val token = call.request.authToken
            val id: String by call.request.queryParameters
            when (val result = getTorrentFileUseCase(token, id)) {
                is Result.Data -> {
                    val (contentDisposition, contentType, bytes) = result.value
                    call.response.header("Content-Disposition", contentDisposition)
                    call.respondBytes(
                        status = HttpStatusCode.OK,
                        bytes = bytes,
                        contentType = ContentType.parse(contentType),
                    )
                }

                else -> respond(result)
            }
        }
    }
}
