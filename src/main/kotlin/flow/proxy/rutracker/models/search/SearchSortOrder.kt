package flow.proxy.rutracker.models.search

enum class SearchSortOrder {
    ASCENDING,
    DESCENDING;

    fun value(): String {
        return when (this) {
            ASCENDING -> "1"
            DESCENDING -> "2"
        }
    }
}
