package flow.proxy.rutracker.domain

import flow.proxy.rutracker.api.RuTrackerApi
import flow.proxy.rutracker.models.Result
import flow.proxy.rutracker.models.forum.Category
import flow.proxy.rutracker.models.search.SearchPage
import flow.proxy.rutracker.models.search.SearchPeriod
import flow.proxy.rutracker.models.search.SearchSortOrder
import flow.proxy.rutracker.models.search.SearchSortType
import flow.proxy.rutracker.models.toResult
import flow.proxy.rutracker.models.topic.Author
import flow.proxy.rutracker.models.topic.Torrent
import flow.proxy.rutracker.models.topic.TorrentStatus
import org.jsoup.Jsoup

class GetSearchPageUseCase(private val api: RuTrackerApi) {

    suspend operator fun invoke(
        token: String,
        searchQuery: String?,
        categories: String?,
        author: String?,
        authorId: String?,
        sortType: SearchSortType?,
        sortOrder: SearchSortOrder?,
        period: SearchPeriod?,
        page: Int?,
    ): Result<SearchPage> {
        return withTokenVerificationUseCase(token) { validToken ->
            withAuthorisedCheckUseCase(
                api.search(
                    validToken, searchQuery, categories, author,
                    authorId, sortType, sortOrder, period, page,
                )
            ) { html -> parseSearchPage(html).toResult() }
        }
    }

    companion object {

        fun parseSearchPage(html: String): SearchPage {
            val doc = Jsoup.parse(html)
            val navigation =
                doc.select("#main_content_wrap > div.bottom_info > div.nav > p:nth-child(1)")
            val currentPage = navigation.select("b:nth-child(1)").toInt(1)
            val totalPages = navigation.select("b:nth-child(2)").toInt(1)
            val torrents = doc.select(".hl-tr").map { element ->
                val id = element.select(".t-title > a").attr("data-topic_id")
                val status = parseTorrentStatusUseCase(element) ?: TorrentStatus.CHECKING
                val titleWithTags = element.select(".t-title > a").toStr()
                val title = getTitle(titleWithTags)
                val tags = getTags(titleWithTags)
                val authorId = getIdFromUrl(element.select(".u-name > a").urlOrNull(), "pid")
                val authorName = element.select(".u-name > a").text()
                val author = authorName.let { Author(id = authorId, name = it) }
                val categoryId = requireIdFromUrl(element.select(".f").url(), "f")
                val categoryName = element.select(".f").toStr()
                val size = formatSize(element.select(".tor-size").attr("data-ts_text").toLong())
                val date = element.select("[style]").attr("data-ts_text").toLongOrNull()
                val seeds = element.select(".seedmed").toIntOrNull()
                val leeches = element.select(".leechmed").toIntOrNull()
                Torrent(
                    id = id,
                    title = title,
                    author = author,
                    category = Category(categoryId, categoryName),
                    tags = tags,
                    status = status,
                    date = date,
                    size = size,
                    seeds = seeds,
                    leeches = leeches
                )
            }
            return SearchPage(currentPage, totalPages, torrents)
        }
    }
}
