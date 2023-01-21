package flow.proxy.rutracker.domain

import flow.proxy.rutracker.api.RuTrackerApi
import flow.proxy.rutracker.models.Result
import flow.proxy.rutracker.models.toResult
import flow.proxy.rutracker.models.topic.File

class GetTorrentFileUseCase(private val api: RuTrackerApi) {

    suspend operator fun invoke(token: String, id: String): Result<File> {
        return withTokenVerificationUseCase(token) { validToken ->
            api.download(validToken, id).toResult()
        }
    }
}
