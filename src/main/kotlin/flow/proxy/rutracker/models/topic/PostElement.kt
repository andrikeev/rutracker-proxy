@file:Suppress("EnumEntryName")

package flow.proxy.rutracker.models.topic

enum class ElementType {
    text,
    box,
    bold,
    italic,
    underscore,
    crossed,
    quote,
    code,
    spoiler,
    image,
    imageAligned,
    link,
    list,
    hr,
    br
}

enum class Alignment {
    start,
    top,
    end,
    bottom
}

abstract class PostElement(
    @Suppress("unused") val type: ElementType
)

data class Text(val value: String) : PostElement(ElementType.text) {
    override fun toString(): String = value
}

data class Box(val children: List<PostElement>) : PostElement(ElementType.box) {
    override fun toString(): String = "Box { $children }"
}

data class Bold(val children: List<PostElement>) : PostElement(ElementType.bold) {
    override fun toString(): String = "Bold { $children }"
}

data class Italic(val children: List<PostElement>) : PostElement(ElementType.italic) {
    override fun toString(): String = "Italic { $children }"
}

data class Underscore(val children: List<PostElement>) : PostElement(ElementType.underscore) {
    override fun toString(): String = "Underscore { $children }"
}

data class Crossed(val children: List<PostElement>) : PostElement(ElementType.crossed) {
    override fun toString(): String = "Crossed { $children }"
}

data class Quote(val title: String, val id: String, val children: List<PostElement>) : PostElement(ElementType.quote) {
    override fun toString(): String = "Quote($title)<id> { $children }"
}

data class Code(val title: String, val children: List<PostElement>) : PostElement(ElementType.code) {
    override fun toString(): String = "Code($title) { $children }"
}

data class Spoiler(val title: String, val children: List<PostElement>) : PostElement(ElementType.spoiler) {
    override fun toString(): String = "Spoiler($title) { $children }"
}

data class Image(val src: String) : PostElement(ElementType.image) {
    override fun toString(): String = "Image { $src }"
}

data class ImageAligned(val src: String, val alignment: Alignment) : PostElement(ElementType.imageAligned) {
    override fun toString(): String = "Image { $src <$alignment> }"
}

data class Link(val src: String, val children: List<PostElement>) : PostElement(ElementType.link) {
    override fun toString(): String = "Link($src) { $children }"
}

data class UList(val children: List<PostElement>) : PostElement(ElementType.list) {
    override fun toString(): String = "UList { $children }"
}

class Hr : PostElement(ElementType.hr) {
    override fun toString(): String = "<hr>"
}

class Br : PostElement(ElementType.br) {
    override fun toString(): String = "<br>"
}
