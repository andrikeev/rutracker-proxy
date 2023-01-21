package flow.proxy.rutracker.domain

import flow.proxy.rutracker.api.RuTrackerApi
import flow.proxy.rutracker.models.Result

class AddCommentUseCase(
    private val api: RuTrackerApi,
    private val withFormTokenUseCase: WithFormTokenUseCase,
) {

    suspend operator fun invoke(token: String, topicId: String, message: String): Result<Nothing> {
        return withFormTokenUseCase(token) { formToken ->
            withAuthorisedCheckUseCase(
                api.postMessage(
                    token,
                    topicId,
                    formToken,
                    message
                )
            ) { html ->
                if (html.contains("Сообщение было успешно отправлено")) {
                    Result.Success
                } else {
                    Result.Error.BadRequest
                }
            }
        }
    }
}
