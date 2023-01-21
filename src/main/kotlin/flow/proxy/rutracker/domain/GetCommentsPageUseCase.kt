package flow.proxy.rutracker.domain

import flow.proxy.rutracker.api.RuTrackerApi
import flow.proxy.rutracker.models.Result
import flow.proxy.rutracker.models.toResult
import flow.proxy.rutracker.models.topic.CommentsPage

class GetCommentsPageUseCase(private val api: RuTrackerApi) {

    suspend operator fun invoke(
        token: String?,
        id: String?,
        pid: String?,
        page: Int?,
    ): Result<CommentsPage> {
        val html = api.getTopic(
            token = token,
            id = id,
            pid = pid,
            page = page
        )
        return when {
            !isTopicExists(html) -> Result.Error.NotFound
            isTopicModerated(html) -> Result.Error.NotFound
            isBlockedForRegion(html) -> Result.Error.NotFound
            else -> parseCommentsPageUseCase(html).toResult()
        }
    }
}
