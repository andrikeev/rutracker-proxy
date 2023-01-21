package flow.proxy.rutracker.routes

import flow.proxy.rutracker.models.Result
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.util.pipeline.*

/**
 * @author andrikeev on 03/10/2021.
 */
inline fun <reified T : Enum<T>> String.toEnumOrNull(): T {
    return enumValueOf(this)
}

inline val ApplicationRequest.authToken: String get() = headers["Auth-Token"].orEmpty()

fun Parameters.require(name: String): String =
    get(name) ?: throw MissingRequestParameterException(name)

fun Parameters.getOrEmpty(name: String): String = get(name).orEmpty()

suspend inline fun <T> PipelineContext<*, ApplicationCall>.respond(result: Result<T>) {
    @Suppress("IMPLICIT_CAST_TO_ANY")
    call.respond(
        status = when (result) {
            is Result.Data,
            is Result.Success -> HttpStatusCode.OK

            is Result.Error.BadRequest -> HttpStatusCode.BadRequest
            is Result.Error.Forbidden -> HttpStatusCode.Forbidden
            is Result.Error.NoConnection -> HttpStatusCode.GatewayTimeout
            is Result.Error.NoData -> HttpStatusCode.BadGateway
            is Result.Error.NotFound -> HttpStatusCode.NotFound
            is Result.Error.Unauthorized -> HttpStatusCode.Unauthorized
        },
        message = when (result) {
            is Result.Data -> result.value
            is Result.Error,
            is Result.Success -> Unit
        } as Any,
    )
}
