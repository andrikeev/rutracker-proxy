package flow.proxy.rutracker.domain

import flow.proxy.rutracker.models.topic.TorrentStatus
import org.jsoup.nodes.Element

fun parseTorrentStatusUseCase(element: Element?): TorrentStatus? {
    return element?.let {
        when {
            element.select(".tor-dup").isNotEmpty() -> TorrentStatus.DUPLICATE
            element.select(".tor-not-approved").isNotEmpty() -> TorrentStatus.NOT_APPROVED
            element.select(".tor-checking").isNotEmpty() -> TorrentStatus.CHECKING
            element.select(".tor-approved").isNotEmpty() -> TorrentStatus.APPROVED
            element.select(".tor-need-edit").isNotEmpty() -> TorrentStatus.NEED_EDIT
            element.select(".tor-closed").isNotEmpty() -> TorrentStatus.CLOSED
            element.select(".tor-no-desc").isNotEmpty() -> TorrentStatus.NO_DESCRIPTION
            element.select(".tor-consumed").isNotEmpty() -> TorrentStatus.CONSUMED
            else -> null
        }
    }
}
