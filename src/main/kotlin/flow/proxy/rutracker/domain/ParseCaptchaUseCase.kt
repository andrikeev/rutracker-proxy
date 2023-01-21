package flow.proxy.rutracker.domain

import flow.proxy.rutracker.models.auth.Captcha
import java.util.regex.Pattern

private val codeRegex =
    Pattern.compile("<input[^>]*name=\"(cap_code_[^\"]+)\"[^>]*value=\"[^\"]*\"[^>]*>")
private val sidRegex = Pattern.compile("<input[^>]*name=\"cap_sid\"[^>]*value=\"([^\"]+)\"[^>]*>")
private val urlRegex = Pattern.compile("<img[^>]*src=\"([^\"]+/captcha/[^\"]+)\"[^>]*>")

fun parseCaptchaUseCase(from: String): Captcha? {
    val codeMatcher = codeRegex.matcher(from)
    val sidMatcher = sidRegex.matcher(from)
    val urlMatcher = urlRegex.matcher(from)
    return if (codeMatcher.find() && sidMatcher.find() && urlMatcher.find()) {
        val captchaUrl = urlMatcher.group(1).let { url ->
            url.takeIf { it.contains("http") } ?: "http://${url.trim('/')}"
        }
        Captcha(sidMatcher.group(1), codeMatcher.group(1), captchaUrl)
    } else {
        null
    }
}
