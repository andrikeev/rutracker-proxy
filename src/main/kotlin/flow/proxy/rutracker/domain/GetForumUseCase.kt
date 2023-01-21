package flow.proxy.rutracker.domain

import flow.proxy.rutracker.api.RuTrackerApi
import flow.proxy.rutracker.models.Result
import flow.proxy.rutracker.models.forum.Category
import flow.proxy.rutracker.models.forum.Forum
import flow.proxy.rutracker.models.toResult
import org.jsoup.Jsoup

class GetForumUseCase(private val api: RuTrackerApi) {

    suspend operator fun invoke(): Result<Forum> {
        if (ForumCache.expired()) {
            ForumCache.cache = System.currentTimeMillis() to parseForumTree(api.getForumTree())
        }
        return ForumCache.cache!!.second.toResult()
    }

    companion object {

        private fun parseForumTree(html: String): Forum {
            val doc = Jsoup.parse(html)
            val categories = mutableListOf<Category>()
            val treeRoots = doc.select(".tree-root")
            treeRoots.forEach { categoryElement ->
                val title = categoryElement.select(".c-title").attr("title")
                val forums = mutableListOf<Category>()
                val forumElements = categoryElement.child(0).child(1).children()
                forumElements.forEach { forumElement ->
                    val forumId = forumElement.child(0).select("a").url()
                    val forumTitle = forumElement.child(0).select("a").toStr()
                    val subforums = mutableListOf<Category>()
                    if (forumElement.children().size > 1) {
                        val subforumElements = forumElement.child(1).children()
                        subforumElements.forEach { subforumElement ->
                            val subforumId = subforumElement.select("a").url()
                            val subforumTitle = subforumElement.toStr()
                            subforums.add(Category(id = subforumId, name = subforumTitle))
                        }
                    }
                    forums.add(Category(id = forumId, name = forumTitle, children = subforums))
                }
                categories.add(Category(name = title, children = forums))
            }
            return Forum(categories)
        }
    }

    private object ForumCache {

        private const val ONE_MONTH: Long = 30L * 24 * 60 * 60 * 1000

        var cache: Pair<Long, Forum>? = null

        fun expired(): Boolean {
            return cache.let { it == null || System.currentTimeMillis() - it.first > ONE_MONTH }
        }
    }
}
