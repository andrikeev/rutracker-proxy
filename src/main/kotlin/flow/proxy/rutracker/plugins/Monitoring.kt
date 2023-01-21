package flow.proxy.rutracker.plugins

import org.slf4j.event.*
import io.ktor.server.application.*
import io.ktor.server.plugins.callloging.*

fun Application.configureMonitoring() {
    install(CallLogging) {
        level = Level.INFO
    }
}
