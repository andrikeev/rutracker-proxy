package flow.proxy.rutracker.domain

import flow.proxy.rutracker.api.RuTrackerApi
import flow.proxy.rutracker.models.Result
import flow.proxy.rutracker.models.toResult
import flow.proxy.rutracker.models.topic.Topic

class GetTopicUseCase(private val api: RuTrackerApi) {

    suspend operator fun invoke(
        token: String?,
        id: String?,
        pid: String?,
        page: Int?,
    ): Result<Topic> {
        val html = api.getTopic(token, id, pid, page)
        return when {
            !isTopicExists(html) -> Result.Error.NotFound
            isTopicModerated(html) -> Result.Error.NotFound
            isBlockedForRegion(html) -> Result.Error.NotFound
            else -> parseTopic(html).toResult()
        }
    }

    private fun parseTopic(html: String) = if (html.contains("magnet-link")) {
        parseTorrentUseCase(html)
    } else {
        parseCommentsPageUseCase(html)
    }
}
