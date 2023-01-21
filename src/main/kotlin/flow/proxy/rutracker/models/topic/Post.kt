package flow.proxy.rutracker.models.topic

data class Post(
    val id: String,
    val author: Author,
    val date: String,
    val children: List<PostElement>
)