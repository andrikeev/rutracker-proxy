package flow.proxy.rutracker.domain

import flow.proxy.rutracker.api.RuTrackerApi
import flow.proxy.rutracker.models.user.Profile
import org.jsoup.Jsoup

class GetProfileUseCase(private val api: RuTrackerApi) {

    suspend operator fun invoke(id: String): Profile {
        val html = api.getProfile(id)
        return parseProfile(html)
    }

    companion object {

        private fun parseProfile(html: String): Profile {
            val doc = Jsoup.parse(html)
            return Profile(
                id = doc.select("#profile-uname").attr("data-uid"),
                name = doc.select("#profile-uname").toStr(),
                avatarUrl = doc.select("#avatar-img > img").attr("src"),
            )
        }
    }
}
