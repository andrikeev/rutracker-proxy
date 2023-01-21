@file:Suppress("unused")

package flow.proxy.rutracker.models.topic

import flow.proxy.rutracker.models.forum.Category

class CommentsPage(
    id: String,
    title: String,
    author: Author? = null,
    category: Category? = null,
    val page: Int,
    val pages: Int,
    val posts: List<Post>
) : Topic(
    id,
    title,
    author,
    category,
)
