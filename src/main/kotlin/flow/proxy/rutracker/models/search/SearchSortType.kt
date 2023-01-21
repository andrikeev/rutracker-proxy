package flow.proxy.rutracker.models.search

enum class SearchSortType {
    DATE,
    TITLE,
    DOWNLOADED,
    SEEDS,
    LEECHES,
    SIZE;

    fun value(): String {
        return when (this) {
            DATE -> "1"
            TITLE -> "2"
            DOWNLOADED -> "4"
            SEEDS -> "10"
            LEECHES -> "11"
            SIZE -> "7"
        }
    }
}
