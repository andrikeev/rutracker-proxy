@file:Suppress("unused")

package flow.proxy.rutracker.models.topic

import flow.proxy.rutracker.models.forum.Category

class Torrent(
    id: String,
    title: String,
    author: Author? = null,
    category: Category? = null,
    val tags: String? = null,
    val status: TorrentStatus? = null,
    val date: Long? = null,
    val size: String? = null,
    val seeds: Int? = null,
    val leeches: Int? = null,
    val magnetLink: String? = null,
    val description: TorrentDescription? = null,
) : Topic(
    id,
    title,
    author,
    category,
)
