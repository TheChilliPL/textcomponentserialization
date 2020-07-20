package me.patrykanuszczyk.textcomponentserialization

import net.md_5.bungee.api.ChatColor
import net.md_5.bungee.api.chat.ClickEvent
import net.md_5.bungee.api.chat.HoverEvent
import net.md_5.bungee.api.chat.TextComponent
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.BookMeta

/**
 * Serializes a text component.
 * @since 1.0
 */
@JvmName("serializeTextComponent")
fun TextComponent?.serialize(): Any? {
    if (this == null) return null

    val map = mutableMapOf<String, Any>()

    if (!text.isNullOrEmpty())
        map["text"] = text

    if (!extra.isNullOrEmpty())
        map["extra"] = extra.map { (it as TextComponent).serialize() }

    if (colorRaw != null)
        map["color"] = serializeChatColor(colorRaw)!!
    if (isBoldRaw != null)
        map["bold"] = isBoldRaw
    if (isItalicRaw != null)
        map["italic"] = isItalicRaw
    if (isUnderlinedRaw != null)
        map["underlined"] = isUnderlinedRaw
    if (isStrikethroughRaw != null)
        map["strikethrough"] = isStrikethroughRaw
    if (isObfuscatedRaw != null)
        map["obfuscated"] = isObfuscatedRaw

    if (insertion != null)
        map["insertion"] = insertion

    if (clickEvent != null) {
        map["clickEvent"] = serializeClickEvent(
            clickEvent
        )!!
    }
    if (hoverEvent != null) {
        map["hoverEvent"] = serializeHoverEvent(
            hoverEvent
        )!!
    }

    if (map.keys.size == 1) {
        when (map.keys.first()) {
            "text" -> return map.getValue("text")
            "extra" -> return map.getValue("extra")
        }
    } else if (map.keys.isEmpty()) {
        return ""
    }

    return map
}

/**
 * Serializes a chat color.
 * @since 1.0
 */
fun serializeChatColor(color: ChatColor?): Any? {
    if (color == null) return null

    return color.name
        .replace('_', ' ')
        .toLowerCase()
}

/**
 * Serializes a click event.
 * @since 1.0
 */
fun serializeClickEvent(event: ClickEvent?): Any? {
    if (event == null) return null

    val action = event.action.name
        .replace('_', ' ')
        .toLowerCase()

    val value = event.value

    return mapOf(
        "action" to action,
        "value" to value
    )
}

/**
 * Serializes a hover event.
 * @since 1.0
 */
fun serializeHoverEvent(event: HoverEvent?): Any? {
    if (event == null) return null

    val action = event.action.name
        .replace('_', ' ')
        .toLowerCase()

    val values = event.value

    val component = (if (values.size == 1)
        values[0]
    else
        TextComponent(*values)) as TextComponent

    val value = component.serialize()

    return mapOf(
        "action" to action,
        "value" to value
    )
}

/**
 * Serializes a book item stack.
 * @since 1.0
 */
fun serializeBook(stack: ItemStack?): Any? {
    if (stack == null) return null
    require(stack.type == Material.WRITTEN_BOOK) { "Book has to be of type WRITTEN_BOOK." }

    val bookMeta = stack.itemMeta as BookMeta
    val map = mutableMapOf<String, Any>()

    map["title"] = bookMeta.title
    map["author"] = bookMeta.author
    map["generation"] = serializeBookGeneration(bookMeta.generation)!!

    map["pages"] = bookMeta.spigot().pages
        .map {
            if (it.size == 1)
                it[0] as TextComponent
            else
                TextComponent(*it)
        }
        .map {
            it.serialize()
        }

    return map
}

/**
 * Serializes book generation.
 * @since 1.0
 */
fun serializeBookGeneration(generation: BookMeta.Generation?): Any? {
    if (generation == null) return null

    return generation.name
        .replace('_', ' ')
        .toLowerCase()
}