package flow.proxy.rutracker.domain

import flow.proxy.rutracker.api.RuTrackerApi
import flow.proxy.rutracker.models.Result

class RemoveFavoriteUseCase(
    private val api: RuTrackerApi,
    private val withFormTokenUseCase: WithFormTokenUseCase,
) {

    suspend operator fun invoke(token: String, id: String): Result<Nothing> {
        return withFormTokenUseCase(token) { formToken ->
            withAuthorisedCheckUseCase(api.removeFavorite(token, id, formToken)) { html ->
                if (html.contains("Тема удалена")) {
                    Result.Success
                } else {
                    Result.Error.BadRequest
                }
            }
        }
    }
}
