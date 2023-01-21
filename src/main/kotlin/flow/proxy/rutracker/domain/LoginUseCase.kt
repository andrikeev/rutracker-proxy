package flow.proxy.rutracker.domain

import flow.proxy.rutracker.api.RuTrackerApi
import flow.proxy.rutracker.models.Result
import flow.proxy.rutracker.models.auth.AuthResponse
import flow.proxy.rutracker.models.auth.User
import flow.proxy.rutracker.models.toResult

class LoginUseCase(
    private val api: RuTrackerApi,
    private val getCurrentProfileUseCase: GetCurrentProfileUseCase,
) {

    suspend operator fun invoke(
        username: String,
        password: String,
        captchaSid: String,
        captchaCode: String,
        captchaValue: String,
    ): Result<AuthResponse> {
        val (token, html) = api.login(
            username,
            password,
            captchaSid,
            captchaCode,
            captchaValue,
        )
        return if (token != null) {
            val (userId, _, avatarUrl) = getCurrentProfileUseCase(token)
            AuthResponse.Success(User(userId, token, avatarUrl)).toResult()
        } else if (html.contains(LOGIN_FORM_KEY)) {
            val captcha = parseCaptchaUseCase(html)
            if (html.contains(WRONG_CREDITS_MESSAGE)) {
                AuthResponse.WrongCredits(captcha)
            } else {
                AuthResponse.CaptchaRequired(captcha)
            }.toResult()
        } else {
            Result.Error.NoData
        }
    }

    private companion object {

        const val LOGIN_FORM_KEY = "login-form"
        const val WRONG_CREDITS_MESSAGE = "неверный пароль"
    }
}
