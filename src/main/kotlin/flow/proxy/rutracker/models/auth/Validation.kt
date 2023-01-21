@file:Suppress("unused")

package flow.proxy.rutracker.models.auth

sealed class ValidationResponse(val validation: Validation) {

    object Ok : ValidationResponse(Validation.OK)

    object Invalid : ValidationResponse(Validation.ERROR)

    enum class Validation {
        OK, ERROR
    }
}
