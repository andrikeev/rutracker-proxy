package flow.proxy.rutracker.domain

import flow.proxy.rutracker.api.RuTrackerApi
import flow.proxy.rutracker.models.Result

class GetIndexUseCase(private val api: RuTrackerApi) {

    suspend operator fun invoke(token: String): Result<Nothing> {
        return withTokenVerificationUseCase(token) { validToken ->
            withAuthorisedCheckUseCase(api.getMainPage(validToken)) {
                Result.Success
            }
        }
    }
}
