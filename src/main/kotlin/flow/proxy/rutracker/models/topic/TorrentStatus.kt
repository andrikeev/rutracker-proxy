package flow.proxy.rutracker.models.topic

enum class TorrentStatus {
    DUPLICATE,
    NOT_APPROVED,
    CHECKING,
    APPROVED,
    NEED_EDIT,
    CLOSED,
    NO_DESCRIPTION,
    CONSUMED,
}
