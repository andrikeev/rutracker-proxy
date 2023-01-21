package flow.proxy.rutracker.models.search

enum class SearchPeriod {
    ALL_TIME,
    TODAY,
    LAST_THREE_DAYS,
    LAST_WEEK,
    LAST_TWO_WEEKS,
    LAST_MONTH;

    fun value(): String {
        return when (this) {
            ALL_TIME -> "-1"
            TODAY -> "1"
            LAST_THREE_DAYS -> "3"
            LAST_WEEK -> "7"
            LAST_TWO_WEEKS -> "14"
            LAST_MONTH -> "32"
        }
    }
}
