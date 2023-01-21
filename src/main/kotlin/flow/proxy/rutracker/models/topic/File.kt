package flow.proxy.rutracker.models.topic

data class File(
    val contentDisposition: String,
    val contentType: String,
    val bytes: ByteArray,
)
