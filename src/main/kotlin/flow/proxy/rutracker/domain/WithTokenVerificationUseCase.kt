package flow.proxy.rutracker.domain

import flow.proxy.rutracker.models.Result

suspend fun <T> withTokenVerificationUseCase(
    token: String,
    block: suspend (token: String) -> Result<T>
): Result<T> {
    return if (token.isEmpty()) {
        Result.Error.Unauthorized
    } else {
        block(token)
    }
}
