package flow.proxy.rutracker.models.auth

sealed class AuthResponse(val status: AuthStatus) {

    data class Success(val user: User) : AuthResponse(AuthStatus.OK)

    data class WrongCredits(val captcha: Captcha?) : AuthResponse(AuthStatus.WRONG_CREDITS)

    data class CaptchaRequired(val captcha: Captcha?) : AuthResponse(AuthStatus.CAPTCHA)
}

enum class AuthStatus {
    OK,
    CAPTCHA,
    WRONG_CREDITS,
}

data class User(
    val id: String,
    val token: String,
    val avatarUrl: String,
)

data class Captcha(
    val id: String,
    val code: String,
    val url: String,
)
