package flow.proxy.rutracker.domain

import flow.proxy.rutracker.models.topic.*
import org.jsoup.nodes.Element
import org.jsoup.nodes.Node
import org.jsoup.nodes.TextNode
import org.jsoup.parser.Tag
import org.jsoup.select.Elements

typealias ElementsList = MutableList<PostElement>

fun elementsList() = mutableListOf<PostElement>()

fun ElementsList.appendElements(elements: Elements) {
    elements.forEach(this::appendElement)
}

fun ElementsList.appendElement(element: Element) {
    if (element.childNodes().isNotEmpty()) {
        element.childNodes().forEach(this::appendNode)
    } else if (element.text().isNotEmpty()) {
        text(element.text())
    }
}

fun ElementsList.appendNode(node: Node) {
    when (node) {
        is Element -> when {
            node.className().contains("post-font") -> appendElement(node)
            node.hasClass("ost-box") -> box { appendElement(node) }
            node.hasClass("post-b") -> bold { appendElement(node) }
            node.hasClass("post-i") -> italic { appendElement(node) }
            node.hasClass("post-u") -> underscore { appendElement(node) }
            node.hasClass("post-s") -> crossed { appendElement(node) }
            node.hasClass("postLink") -> link(node.url()) { appendElement(node) }
            node.hasClass("postImg") && !node.hasClass("postImgAligned") -> image(node.attr("title"))
            node.hasClass("postImg") && node.hasClass("postImgAligned") -> imageAligned(
                node.attr("title"), when {
                    node.hasClass("img-left") -> Alignment.start
                    node.hasClass("img-top") -> Alignment.top
                    node.hasClass("img-right") -> Alignment.end
                    node.hasClass("img-bottom") -> Alignment.bottom
                    else -> Alignment.start
                }
            )

            node.hasClass("post-ul") -> uList { appendElement(node) }
            node.hasClass("c-wrap") -> code(
                node.select(".c-head").text()
            ) { appendElements(node.select(".c-body")) }

            node.hasClass("sp-wrap") -> spoiler(
                node.select(".sp-head").text()
            ) { appendElements(node.select(".sp-body")) }

            node.hasClass("q-wrap") -> quote(
                node.select(".q-head").text(),
                node.select(".q-post").text()
            ) {
                node.select(".q-post").remove()
                appendElements(node.select(".q"))
            }

            node.hasClass("post-hr") -> hr()
            node.hasClass("post-br") || node.tag() == Tag.valueOf("br") -> br()
            else -> appendElement(node)
        }

        is TextNode -> text(node.text())
    }
}

fun ElementsList.text(value: String) {
    if (value.isNotBlank()) {
        add(Text(value.replace("\n", "").replace("\t", "").trim()))
    }
}

fun ElementsList.bold(block: ElementsList.() -> Unit) {
    add(Bold(elementsList().apply(block)))
}

fun ElementsList.italic(block: ElementsList.() -> Unit) {
    add(Italic(elementsList().apply(block)))
}

fun ElementsList.underscore(block: ElementsList.() -> Unit) {
    add(Underscore(elementsList().apply(block)))
}

fun ElementsList.crossed(block: ElementsList.() -> Unit) {
    add(Crossed(elementsList().apply(block)))
}

fun ElementsList.box(block: ElementsList.() -> Unit) {
    add(Box(elementsList().apply(block)))
}

fun ElementsList.uList(block: ElementsList.() -> Unit) {
    add(UList(elementsList().apply(block)))
}

fun ElementsList.code(title: String, block: ElementsList.() -> Unit) {
    add(Code(title, elementsList().apply(block)))
}

fun ElementsList.spoiler(title: String, block: ElementsList.() -> Unit) {
    add(Spoiler(title, elementsList().apply(block)))
}

fun ElementsList.quote(title: String, id: String, block: ElementsList.() -> Unit) {
    add(Quote(title, id, elementsList().apply(block)))
}

fun ElementsList.link(src: String, block: ElementsList.() -> Unit) {
    add(Link(src, elementsList().apply(block)))
}

fun ElementsList.image(src: String) {
    add(Image(src))
}

fun ElementsList.imageAligned(src: String, alignment: Alignment) {
    add(ImageAligned(src, alignment))
}

fun ElementsList.hr() {
    add(Hr())
}

fun ElementsList.br() {
    add(Br())
}
