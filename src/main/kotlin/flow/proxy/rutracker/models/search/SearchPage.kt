package flow.proxy.rutracker.models.search

import flow.proxy.rutracker.models.topic.Torrent

data class SearchPage(
    val page: Int,
    val pages: Int,
    val torrents: List<Torrent>
)
