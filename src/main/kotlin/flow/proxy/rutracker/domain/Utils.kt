package flow.proxy.rutracker.domain

import org.jsoup.nodes.Element
import org.jsoup.select.Elements
import java.net.URI
import java.util.regex.Pattern
import kotlin.math.ln
import kotlin.math.pow

fun Element?.toInt(default: Int = 0): Int = this?.text()?.toIntOrNull() ?: default

fun Elements?.toInt(default: Int = 0): Int = this?.text()?.toIntOrNull() ?: default

fun Elements?.toIntOrNull(): Int? = this?.text()?.toIntOrNull()

fun Element?.toStr(): String = this?.text().orEmpty()

fun Elements?.toStr(): String = this?.text().orEmpty()

fun Elements?.urlOrNull(): String? = this?.attr("href")

fun Element?.urlOrNull(): String? = this?.attr("href")

fun Element?.url(): String = requireNotNull(this.urlOrNull()) { "url not found" }

fun Elements?.url(): String = requireNotNull(this.urlOrNull()) { "url not found" }

fun getIdFromUrl(url: String?, key: String): String? {
    return url?.let { parseUrl(url)[key]?.firstOrNull() }
}

fun requireIdFromUrl(url: String, key: String): String {
    return requireNotNull(parseUrl(url)[key]?.firstOrNull()) { "query param not found in url" }
}

fun isTopicExists(html: String): Boolean {
    return !html.contains("Тема не найдена") &&
            !html.contains("Тема находится в мусорке") &&
            !html.contains("Ошибочный запрос: не указан topic_id")
}

fun isBlockedForRegion(html: String): Boolean {
    return html.contains("Извините, раздача недоступна для вашего региона")
}

fun isTopicModerated(html: String): Boolean {
    return html.contains("Раздача ожидает проверки модератором")
}

fun getTitle(titleWithTags: String): String {
    return titleWithTags.replace("\\[[^]]*]".toRegex(), "").replace("  ", " ").trim()
}

fun getTags(titleWithTags: String): String {
    val stringBuilder = StringBuilder()
    val matcher = Pattern.compile("(\\[[^]]*])").matcher(titleWithTags)
    while (matcher.find()) stringBuilder.append(matcher.group(1))
    return stringBuilder.toString()
}

fun formatSize(sizeBytes: Long): String {
    if (sizeBytes < 1024) {
        return "$sizeBytes B"
    }
    val exp = (ln(sizeBytes.toDouble()) / ln(1024.0)).toInt()
    val pre = "KMGTPE"[exp - 1].toString()
    return String.format("%.1f %sB", sizeBytes / 1024.0.pow(exp.toDouble()), pre)
}

private fun parseUrl(url: String): Map<String, List<String>> {
    return try {
        URI.create(url)
            .query.split("&")
            .associate { queryParam ->
                val split = queryParam.split("=")
                split[0] to split.drop(1)
            }
    } catch (e: Exception) {
        emptyMap()
    }
}
