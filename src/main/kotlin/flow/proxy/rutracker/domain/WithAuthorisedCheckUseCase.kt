package flow.proxy.rutracker.domain

import flow.proxy.rutracker.models.Result

suspend fun <T> withAuthorisedCheckUseCase(
    html: String,
    mapper: suspend (String) -> Result<T>,
): Result<T> = if (html.contains("logged-in-username")) {
    mapper(html)
} else {
    Result.Error.Unauthorized
}
