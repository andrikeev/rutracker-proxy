package flow.proxy.rutracker.models.topic

import flow.proxy.rutracker.models.forum.Category

open class Topic(
    open val id: String,
    open val title: String,
    open val author: Author? = null,
    open val category: Category? = null
)
