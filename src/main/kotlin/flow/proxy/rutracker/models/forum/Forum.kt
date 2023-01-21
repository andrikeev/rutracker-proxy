package flow.proxy.rutracker.models.forum

import flow.proxy.rutracker.models.topic.Topic

data class Forum(val children: List<Category>)

data class Category(
    val id: String? = null,
    val name: String,
    val children: List<Category>? = null
)

data class CategoryPage(
    val category: Category,
    val page: Int,
    val pages: Int,
    val children: List<Category>? = null,
    val topics: List<Topic>? = null,
)
