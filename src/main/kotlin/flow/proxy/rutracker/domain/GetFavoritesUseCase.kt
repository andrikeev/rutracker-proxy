package flow.proxy.rutracker.domain

import flow.proxy.rutracker.api.RuTrackerApi
import flow.proxy.rutracker.models.Result
import flow.proxy.rutracker.models.forum.Category
import flow.proxy.rutracker.models.toResult
import flow.proxy.rutracker.models.topic.Author
import flow.proxy.rutracker.models.topic.Topic
import flow.proxy.rutracker.models.topic.Torrent
import flow.proxy.rutracker.models.user.Favorites
import org.jsoup.Jsoup

class GetFavoritesUseCase(private val api: RuTrackerApi) {

    suspend operator fun invoke(token: String): Result<Favorites> {
        return withTokenVerificationUseCase(token) { validToken ->
            withAuthorisedCheckUseCase(api.getFavorites(validToken, 1)) { html ->
                val pagesCount = parsePagesCount(html)
                Favorites(
                    (listOf(parseFavorites(html)) +
                            (2..pagesCount)
                                .map { page -> api.getFavorites(token, page) }
                                .map(::parseFavorites))
                        .flatten()
                ).toResult()
            }
        }
    }

    companion object {

        private fun parsePagesCount(html: String): Int {
            val doc = Jsoup.parse(html)
            val navigation = doc.select("#pagination")
            val currentPage = navigation.select("b").toInt(1)
            return maxOf(
                navigation
                    .select(".pg")
                    .takeLast(2)
                    .firstOrNull()
                    .toInt(1),
                currentPage,
            )
        }

        private fun parseFavorites(html: String): List<Topic> {
            return Jsoup
                .parse(html)
                .select(".hl-tr")
                .map { element ->
                    val id = element.select(".topic-selector").attr("data-topic_id")
                    val fullTitle = element.select(".torTopic.ts-text").toStr()
                    val title = getTitle(fullTitle)
                    val tags = getTags(fullTitle)
                    val status = parseTorrentStatusUseCase(element)
                    val authorId = getIdFromUrl(element.select(".topicAuthor").urlOrNull(), "u")
                    val authorName = element.select(".topicAuthor > .topicAuthor").text()
                    val author = authorName.let { Author(id = authorId, name = it) }
                    val categoryId = requireIdFromUrl(
                        element.select(".t-forum-cell").select("a").last().url(),
                        "f"
                    )
                    val categoryName = element.select(".t-forum-cell > .ts-text").toStr()
                    val category = Category(categoryId, categoryName)
                    if (status != null) {
                        val size = element.select(".f-dl").text()
                        val seeds = element.select(".seedmed").toIntOrNull()
                        val leeches = element.select(".leechmed").toIntOrNull()
                        Torrent(
                            id = id,
                            title = title,
                            author = author,
                            category = category,
                            tags = tags,
                            status = status,
                            size = size,
                            seeds = seeds,
                            leeches = leeches
                        )
                    } else {
                        Topic(
                            id = id,
                            title = fullTitle,
                            author = author,
                            category = category
                        )
                    }
                }
        }
    }
}
