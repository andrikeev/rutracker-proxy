package flow.proxy.rutracker.domain

import flow.proxy.rutracker.api.RuTrackerApi
import flow.proxy.rutracker.models.Result
import flow.proxy.rutracker.models.forum.Category
import flow.proxy.rutracker.models.forum.CategoryPage
import flow.proxy.rutracker.models.toResult
import flow.proxy.rutracker.models.topic.Author
import flow.proxy.rutracker.models.topic.Topic
import flow.proxy.rutracker.models.topic.Torrent
import org.jsoup.Jsoup

class GetCategoryPageUseCase(private val api: RuTrackerApi) {

    suspend operator fun invoke(id: String, page: Int?): Result<CategoryPage> {
        val html = api.getForum(id, page)
        return when {
            !isForumExists(html) -> Result.Error.NotFound
            !isForumAvailableForUser(html) -> Result.Error.Forbidden
            else -> parseCategoryPage(html, id).toResult()
        }
    }

    companion object {

        private fun isForumExists(html: String): Boolean {
            return !html.contains("Ошибочный запрос: не задан forum_id") and
                    !html.contains("Такого форума не существует")
        }

        private fun isForumAvailableForUser(html: String): Boolean {
            return !html.contains("Извините, только пользователи со специальными правами")
        }

        private fun parseCategoryPage(html: String, forumId: String): CategoryPage {
            val doc = Jsoup.parse(html)
            val currentPage = doc.select("#pagination > p:nth-child(1) > b:nth-child(1)").toInt(1)
            val totalPages = doc.select("#pagination > p:nth-child(1) > b:nth-child(2)").toInt(1)
            val forumName = doc.select(".maintitle").toStr()

            val subforumNodes = doc.select(".forumlink > a")
            val children = mutableListOf<Category>()
            for (subforumNode in subforumNodes) {
                val id = requireIdFromUrl(subforumNode.url(), "f")
                val name = subforumNode.toStr()
                val subforum = Category(id, name)
                children.add(subforum)
            }

            val topicNodes = doc.select(".hl-tr")
            val topics = mutableListOf<Topic>()
            for (topicNode in topicNodes) {
                val id = topicNode.select("td").attr("id")
                val authorId = getIdFromUrl(topicNode.select("a.topicAuthor").url(), "u")
                val authorName = topicNode.select("a.topicAuthor").toStr()
                val seeds = topicNode.select(".seedmed").toIntOrNull()
                val leeches = topicNode.select(".leechmed").toIntOrNull()
                val size = topicNode.select(".f-dl").text().replace("\u00a0", " ")
                val fullTitle = topicNode.select(".tt-text").toStr()
                val title = getTitle(fullTitle)
                val tags = getTags(fullTitle)
                val status = parseTorrentStatusUseCase(topicNode)
                val author = if (authorName.isBlank()) {
                    Author(name = topicNode.select(".vf-col-author").toStr())
                } else {
                    Author(id = authorId, name = authorName)
                }
                if (status == null) {
                    topics.add(Topic(id, fullTitle, author))
                } else {
                    topics.add(
                        Torrent(
                            id = id,
                            title = title,
                            tags = tags,
                            status = status,
                            author = author,
                            size = size,
                            seeds = seeds,
                            leeches = leeches
                        )
                    )
                }
            }
            return CategoryPage(
                category = Category(forumId, forumName),
                page = currentPage,
                pages = totalPages,
                children = children,
                topics = topics,
            )
        }
    }
}
