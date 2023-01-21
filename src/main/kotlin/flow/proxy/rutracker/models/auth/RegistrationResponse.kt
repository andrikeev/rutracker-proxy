package flow.proxy.rutracker.models.auth

sealed class RegistrationResponse(val status: RegistrationStatus) {

    object Success : RegistrationResponse(RegistrationStatus.OK)

    data class Error(val captcha: Captcha?) : RegistrationResponse(RegistrationStatus.ERROR)
}

enum class RegistrationStatus {
    OK,
    ERROR,
}
