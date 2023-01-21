package flow.proxy.rutracker.domain

import flow.proxy.rutracker.api.RuTrackerApi
import flow.proxy.rutracker.models.Result
import java.util.regex.Pattern

class WithFormTokenUseCase(private val api: RuTrackerApi) {

    suspend operator fun <T> invoke(
        token: String,
        block: suspend (formToken: String) -> Result<T>,
    ): Result<T> {
        return if (token.isEmpty()) {
            return Result.Error.Unauthorized
        } else {
            val formToken = parseFormToken(api.getMainPage(token))
            if (formToken.isEmpty()) {
                return Result.Error.Unauthorized
            } else {
                block(formToken)
            }
        }
    }

    companion object {

        private val formTokenRegex = Pattern.compile("form_token: '(.*)',")
        private fun parseFormToken(html: String): String {
            val matcher = formTokenRegex.matcher(html)
            return if (matcher.find()) {
                matcher.group(1)
            } else {
                ""
            }
        }
    }
}
