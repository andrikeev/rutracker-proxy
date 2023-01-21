package flow.proxy.rutracker.domain

import flow.proxy.rutracker.api.RuTrackerApi
import flow.proxy.rutracker.models.user.Profile
import org.jsoup.Jsoup

class GetCurrentProfileUseCase(
    private val api: RuTrackerApi,
    private val getProfileUseCase: GetProfileUseCase,
) {

    suspend operator fun invoke(token: String): Profile {
        return getProfileUseCase(parseUserId(api.getMainPage(token)))
    }

    companion object {

        private fun parseUserId(html: String): String {
            val doc = Jsoup.parse(html)
            val userProfileUrl =
                requireNotNull(
                    doc.select("#logged-in-username").urlOrNull()
                ) { "profile url is null" }
            return requireNotNull(getIdFromUrl(userProfileUrl, "u")) { "user id not found" }
        }
    }
}
