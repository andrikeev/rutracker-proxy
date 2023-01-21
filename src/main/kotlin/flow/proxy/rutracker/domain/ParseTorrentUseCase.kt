package flow.proxy.rutracker.domain

import flow.proxy.rutracker.models.forum.Category
import flow.proxy.rutracker.models.topic.Author
import flow.proxy.rutracker.models.topic.Torrent
import flow.proxy.rutracker.models.topic.TorrentDescription
import org.jsoup.Jsoup

fun parseTorrentUseCase(html: String): Torrent {
    val doc = Jsoup.parse(html)
    val id = requireIdFromUrl(doc.select("#topic-title").url(), "t")
    val author = doc.select(".nick").first()?.text()?.let {
        val authorId =
            getIdFromUrl(doc.select(".poster_btn").select(".txtb").first().urlOrNull(), "u")
        Author(id = authorId, name = it)
    }
    val title = getTitle(doc.select("#topic-title").toStr())
    val tags = getTags(doc.select("#topic-title").toStr())
    val categoryNode = doc.select(".nav.w100.pad_2").select("a").last()
    val categoryId = requireIdFromUrl(categoryNode.url(), "f")
    val categoryName = categoryNode.toStr()
    val magnetLink = doc.select(".magnet-link").url()
    val header = doc.select("table.forumline.dl_list > tbody > tr")
    val seeds = header.select(".seed > b").toIntOrNull()
    val leeches = header.select(".leech > b").toIntOrNull()
    val status = parseTorrentStatusUseCase(doc.select("#tor-status-resp").first())
    val size = if (html.contains("logged-in-username")) {
        doc.select("#tor-size-humn").toStr()
    } else {
        doc.select(".attach_link > ul > li:nth-child(2)").toStr()
    }
    return Torrent(
        id = id,
        title = title,
        tags = tags,
        status = status,
        category = Category(categoryId, categoryName),
        author = author,
        seeds = seeds,
        leeches = leeches,
        size = size,
        magnetLink = magnetLink,
        description = parseTorrentDescription(html)
    )
}

private fun parseTorrentDescription(html: String): TorrentDescription {
    val doc = Jsoup.parse(html)
    return try {
        val firstPost = doc.select("tbody[id^=post]").first()?.select(".post_body")
        torrentDescription {
            appendElements(requireNotNull(firstPost))
        }
    } catch (e: Throwable) {
        torrentDescription {}
    }
}

private fun torrentDescription(block: ElementsList.() -> Unit): TorrentDescription {
    return TorrentDescription(elementsList().apply(block))
}
