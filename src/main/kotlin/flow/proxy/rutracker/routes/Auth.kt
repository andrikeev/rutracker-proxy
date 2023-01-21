package flow.proxy.rutracker.routes

import flow.proxy.rutracker.di.inject
import flow.proxy.rutracker.domain.LoginUseCase
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.routing.*

fun Application.configureAuthRoutes() {
    val loginUseCase by inject<LoginUseCase>()

    routing {
        post("/login") {
            with (call.receiveParameters()) {
                val username = require("username")
                val password = require("password")
                val captchaSid = getOrEmpty("cap_sid")
                val captchaCode = getOrEmpty("cap_code")
                val captchaValue = getOrEmpty("cap_val")
                respond(
                    loginUseCase.invoke(
                        username,
                        password,
                        captchaSid,
                        captchaCode,
                        captchaValue,
                    )
                )
            }
        }
//        get("/registrationForm") {
//        }
//
//        get("/validateUsername") {
//            val username: String by call.request.queryParameters
//            val json = api.validateUsername(username)
//            if (json.contains("errorMessage")) {
//                respondOk(ValidationResponse.Ok)
//            } else {
//                respondOk(ValidationResponse.Invalid)
//            }
//        }
//
//        get("/validateEmail") {
//            val email: String by call.request.queryParameters
//            val json = api.validateEmail(email)
//            if (json.contains("errorMessage")) {
//                respondOk(ValidationResponse.Ok)
//            } else {
//                respondOk(ValidationResponse.Invalid)
//            }
//        }
//
//        post("/register") {
//            val postParameters: Parameters = call.receiveParameters()
//            val username = postParameters.require("username")
//            val password = postParameters.require("password")
//            val email = postParameters.require("email")
//            val captchaSid = postParameters.require("cap_sid")
//            val captchaCode = postParameters.require("cap_code")
//            val captchaValue = postParameters.require("cap_val")
//            val html =
//                api.register(username, password, email, captchaSid, captchaCode, captchaValue)
//            if (html.contains("Спасибо за регистрацию")) {
//                respondOk(RegistrationResponse.Success)
//            } else {
//                respondOk(RegistrationResponse.Error(CaptchaParser.invoke(html)))
//            }
//        }
    }
}
