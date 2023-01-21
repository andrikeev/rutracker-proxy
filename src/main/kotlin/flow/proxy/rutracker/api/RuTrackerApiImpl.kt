package flow.proxy.rutracker.api

import flow.proxy.rutracker.models.search.SearchPeriod
import flow.proxy.rutracker.models.search.SearchSortOrder
import flow.proxy.rutracker.models.search.SearchSortType
import flow.proxy.rutracker.models.topic.File
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.client.statement.*
import io.ktor.http.*
import java.net.URLEncoder

class RuTrackerApiImpl(httpClientFactory: HttpClientFactory) : RuTrackerApi {

    private val httpClient: HttpClient = httpClientFactory.create()

    override suspend fun getMainPage(token: String) = httpClient.get(INDEX) {
        header(COOKIE_HEADER, token)
    }.bodyAsText()

    override suspend fun login(
        username: String,
        password: String,
        captchaSid: String,
        captchaCode: String,
        captchaValue: String,
    ): Pair<String?, String> = httpClient.submitForm(
        url = LOGIN,
        formParameters = Parameters.build {
            append("login_username", username.toCp1251())
            append("login_password", password.toCp1251())
            append("login", "Вход")
            append("cap_sid", captchaSid)
            append(captchaCode, captchaValue)
        },
    ).let { response ->
        val cookie = response.headers.getAll("Set-Cookie").orEmpty()
        val token = cookie.firstOrNull { !it.contains("bb_ssl") }
        token to response.bodyAsText()
    }

    override suspend fun getRegistrationForm() = httpClient.post(PROFILE) {
        parameter("mode", "register")
        formData { append("reg_agreed", "1") }
    }.bodyAsText()

    override suspend fun validateUsername(username: String) = httpClient.post(AJAX) {
        formData {
            append("action", "validateRegistrationData")
            append("validator", "username")
            append("value", username)
            append("validatedElementId", "reg-username")
            append("errorElementId", "reg-error-username")
            append("form_token", "")
        }
    }.bodyAsText()

    override suspend fun validateEmail(email: String) = httpClient.post(AJAX) {
        formData {
            append("action", "validateRegistrationData")
            append("validator", "email")
            append("value", email)
            append("validatedElementId", "reg-user-email")
            append("errorElementId", "reg-error-email")
            append("form_token", "")
        }
    }.bodyAsText()

    override suspend fun register(
        username: String,
        password: String,
        email: String,
        captchaSid: String,
        captchaCode: String,
        captchaValue: String,
    ) = httpClient.post(PROFILE) {
        parameter("mode", "register")
        formData {
            append("reg_agreed", "1")
            append("username", username)
            append("new_pass", password)
            append("user_email", email)
            append("cap_sid", captchaSid)
            append(captchaCode, captchaValue)
            append("user_flag_id", "0")
            append("user_timezone_x2", "6")
            append("user_gender_id", "0")
        }
    }.bodyAsText()

    override suspend fun search(
        token: String,
        searchQuery: String?,
        categories: String?,
        author: String?,
        authorId: String?,
        sortType: SearchSortType?,
        sortOrder: SearchSortOrder?,
        period: SearchPeriod?,
        page: Int?,
    ) = httpClient.get(TRACKER) {
        header(COOKIE_HEADER, token)
        parameter("nm", searchQuery)
        parameter("f", categories)
        parameter("pn", author)
        parameter("pid", authorId)
        parameter("o", sortType?.value())
        parameter("s", sortOrder?.value())
        parameter("tm", period?.value())
        parameter("start", page?.let { (50 * (page - 1)).toString() })
    }.bodyAsText()

    override suspend fun getCategories() = httpClient.get(INDEX).bodyAsText()

    override suspend fun getCategory(id: String) = httpClient.get(INDEX) {
        parameter("c", id)
    }.bodyAsText()

    override suspend fun getForum(
        id: String,
        page: Int?,
    ) = httpClient.get(FORUM) {
        parameter("f", id)
        parameter("start", page?.let { (50 * (page - 1)).toString() })
    }.bodyAsText()

    override suspend fun getTopic(
        token: String?,
        id: String?,
        pid: String?,
        page: Int?,
    ) = httpClient.get(TOPIC) {
        header(COOKIE_HEADER, token)
        if (!id.isNullOrEmpty()) {
            parameter("t", id)
        } else {
            parameter("p", pid)
        }
        parameter("start", page?.let { (30 * (page - 1)).toString() })
    }.bodyAsText()

    override suspend fun download(token: String, id: String) = httpClient.get(DOWNLOAD) {
        header(COOKIE_HEADER, token)
        parameter("t", id)
    }.let { response ->
        File(
            contentDisposition = response.headers["Content-Disposition"].orEmpty(),
            contentType = response.headers["Content-Type"].orEmpty(),
            bytes = response.readBytes(),
        )
    }

    override suspend fun getForumTree() = httpClient.get(INDEX) {
        parameter("map", "0")
    }.bodyAsText()

    override suspend fun getProfile(userId: String) = httpClient.get(PROFILE) {
        parameter("mode", "viewprofile")
        parameter("u", userId)
    }.bodyAsText()

    override suspend fun postMessage(
        token: String,
        topicId: String,
        formToken: String,
        message: String,
    ) = httpClient.post(POSTING) {
        header(COOKIE_HEADER, token)
        formData {
            append("mode", "reply")
            append("submit_mode", "submit")
            append("t", topicId)
            append("form_token", formToken)
            append("message", message.toCp1251())
        }
    }.bodyAsText()

    override suspend fun getFavorites(token: String, page: Int?) = httpClient.get(BOOKMARKS) {
        header(COOKIE_HEADER, token)
        parameter("start", page?.let { (50 * (page - 1)).toString() })

    }.bodyAsText()

    override suspend fun addFavorite(
        token: String,
        id: String,
        formToken: String,
    ) = httpClient.post(BOOKMARKS) {
        header(COOKIE_HEADER, token)
        formData {
            append("action", "bookmark_add")
            append("topic_id", id)
            append("form_token", formToken)
        }
    }.bodyAsText()

    override suspend fun removeFavorite(
        token: String,
        id: String,
        formToken: String,
    ) = httpClient.post(BOOKMARKS) {
        header(COOKIE_HEADER, token)
        formData {
            append("action", "bookmark_delete")
            append("topic_id", id)
            append("form_token", formToken)
            append("request_origin", "from_topic_page")
        }
    }.bodyAsText()

    override suspend fun getFutureDownloads(token: String, page: Int?) = httpClient.get(SEARCH) {
        header(COOKIE_HEADER, token)
        parameter("future_dls", null)
        parameter("start", page?.let { (50 * (page - 1)).toString() })
    }.bodyAsText()

    override suspend fun addFutureDownload(
        token: String,
        id: String,
        formToken: String,
    ) = httpClient.post(AJAX) {
        header(COOKIE_HEADER, token)
        parameter("action", "add_future_dl")
        parameter("topic_id", id)
        parameter("form_token", formToken)
    }.bodyAsText()

    override suspend fun removeFutureDownload(
        token: String,
        id: String,
        formToken: String,
    ) = httpClient.post(AJAX) {
        header(COOKIE_HEADER, token)
        parameter("action", "del_future_dl")
        parameter("topic_id", id)
        parameter("form_token", formToken)
    }.bodyAsText()

    private companion object {

        const val LOGIN: String = "login.php"
        const val PROFILE: String = "profile.php"
        const val INDEX: String = "index.php"
        const val TRACKER: String = "tracker.php"
        const val FORUM: String = "viewforum.php"
        const val TOPIC: String = "viewtopic.php"
        const val POSTING: String = "posting.php"
        const val BOOKMARKS: String = "bookmarks.php"
        const val AJAX: String = "ajax.php"
        const val SEARCH: String = "search.php"
        const val DOWNLOAD: String = "dl.php"

        const val COOKIE_HEADER: String = "Cookie"
    }

    private fun String.toCp1251(): String = URLEncoder.encode(this, "Windows-1251")
}
