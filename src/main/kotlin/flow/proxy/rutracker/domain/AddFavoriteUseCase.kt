package flow.proxy.rutracker.domain

import flow.proxy.rutracker.api.RuTrackerApi
import flow.proxy.rutracker.models.Result

class AddFavoriteUseCase(
    private val api: RuTrackerApi,
    private val withFormTokenUseCase: WithFormTokenUseCase,
) {

    suspend operator fun invoke(token: String, id: String): Result<Nothing> {
        return withFormTokenUseCase(token) { formToken ->
            withAuthorisedCheckUseCase(api.addFavorite(token, id, formToken)) { html ->
                if (html.contains("Тема добавлена")) {
                    Result.Success
                } else {
                    Result.Error.BadRequest
                }
            }
        }
    }
}
