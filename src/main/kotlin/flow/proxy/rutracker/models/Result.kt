package flow.proxy.rutracker.models

sealed interface Result<out T> {
    object Success : Result<Nothing>
    data class Data<T>(val value: T) : Result<T>
    sealed class Error : Result<Nothing>, Throwable() {
        object BadRequest : Error()
        object Forbidden : Error()
        object NoData : Error()
        object NoConnection : Error()
        object NotFound : Error()
        object Unauthorized : Error()
    }
}

fun <T> T.toResult() = Result.Data(this)
