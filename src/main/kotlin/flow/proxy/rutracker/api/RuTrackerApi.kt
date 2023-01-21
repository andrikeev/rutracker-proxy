package flow.proxy.rutracker.api

import flow.proxy.rutracker.models.search.SearchPeriod
import flow.proxy.rutracker.models.search.SearchSortOrder
import flow.proxy.rutracker.models.search.SearchSortType
import flow.proxy.rutracker.models.topic.File

interface RuTrackerApi {

    suspend fun getMainPage(token: String): String
    suspend fun login(
        username: String,
        password: String,
        captchaSid: String,
        captchaCode: String,
        captchaValue: String,
    ): Pair<String?, String>

    suspend fun getRegistrationForm(): String
    suspend fun validateUsername(username: String): String
    suspend fun validateEmail(email: String): String
    suspend fun register(
        username: String,
        password: String,
        email: String,
        captchaSid: String,
        captchaCode: String,
        captchaValue: String,
    ): String

    suspend fun search(
        token: String,
        searchQuery: String?,
        categories: String?,
        author: String?,
        authorId: String?,
        sortType: SearchSortType?,
        sortOrder: SearchSortOrder?,
        period: SearchPeriod?,
        page: Int?,
    ): String

    suspend fun getCategories(): String
    suspend fun getCategory(id: String): String
    suspend fun getForum(
        id: String,
        page: Int?
    ): String

    suspend fun getTopic(
        token: String?,
        id: String? = null,
        pid: String? = null,
        page: Int? = null,
    ): String

    suspend fun download(token: String, id: String): File
    suspend fun getForumTree(): String
    suspend fun getProfile(userId: String): String
    suspend fun postMessage(
        token: String,
        topicId: String,
        formToken: String,
        message: String
    ): String

    suspend fun getFavorites(token: String, page: Int?): String
    suspend fun addFavorite(token: String, id: String, formToken: String): String
    suspend fun removeFavorite(token: String, id: String, formToken: String): String
    suspend fun getFutureDownloads(token: String, page: Int?): String
    suspend fun addFutureDownload(token: String, id: String, formToken: String): String
    suspend fun removeFutureDownload(token: String, id: String, formToken: String): String
}