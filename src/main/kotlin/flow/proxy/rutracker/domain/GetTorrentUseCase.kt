package flow.proxy.rutracker.domain

import flow.proxy.rutracker.api.RuTrackerApi
import flow.proxy.rutracker.models.Result
import flow.proxy.rutracker.models.toResult
import flow.proxy.rutracker.models.topic.Topic

class GetTorrentUseCase(private val api: RuTrackerApi) {

    suspend operator fun invoke(token: String?, id: String?): Result<Topic> {
        val html = api.getTopic(token, id)
        return when {
            !isTopicExists(html) -> Result.Error.NotFound
            isTopicModerated(html) -> Result.Error.NotFound
            isBlockedForRegion(html) -> Result.Error.NotFound
            else -> parseTorrentUseCase(html).toResult()
        }
    }
}
