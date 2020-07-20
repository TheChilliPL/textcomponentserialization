package me.patrykanuszczyk.textcomponentserialization

import net.md_5.bungee.api.ChatColor
import net.md_5.bungee.api.chat.ClickEvent
import net.md_5.bungee.api.chat.HoverEvent
import net.md_5.bungee.api.chat.TextComponent
import org.bukkit.Material
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.BookMeta

/**
 * Deserializes a text component.
 * @since 1.0
 */
fun deserializeTextComponent(obj: Any?): TextComponent? {
    val map = when (obj) {
        null -> return null
        is ConfigurationSection -> obj.getValues(true)
        is Map<*, *> -> obj.toMapOf<String, Any>()
        is String -> return TextComponent(obj)
        is List<*> -> mapOf("extra" to obj)
        else -> throw IllegalArgumentException("Couldn't deserialize text component from ${obj::class.qualifiedName}!")
    }

    val component = TextComponent()

    component.text = map["text"] as String? ?: ""

    if (map["extra"] != null)
        component.extra = (map["extra"] as List<*>)
            .map {
                deserializeTextComponent(
                    it ?: throw NullPointerException("Extra object is null!")
                )
            }

    val color = deserializeChatColor(map["color"])

    component.color = color
    component.setBold(map["bold"] as Boolean?)
    component.setItalic(map["italic"] as Boolean?)
    component.setUnderlined(map["underlined"] as Boolean?)
    component.setStrikethrough(map["strikethrough"] as Boolean?)
    component.setObfuscated(map["obfuscated"] as Boolean?)

    component.insertion = map["insertion"] as String?

    component.clickEvent =
        deserializeClickEvent(map["clickEvent"])
    component.hoverEvent =
        deserializeHoverEvent(map["hoverEvent"])

    return component
}

/**
 * Deserializes a chat color.
 * @since 1.0
 */
fun deserializeChatColor(obj: Any?): ChatColor? {
    if (obj == null) return null
    require(obj is String) { "Color has to be a string!" }

    val canonicalName = obj
        .replace(' ', '_')
        .toUpperCase()

    return ChatColor.valueOf(canonicalName)
}

/**
 * Deserializes a click event.
 * @since 1.0
 */
fun deserializeClickEvent(obj: Any?): ClickEvent? {
    if (obj == null) return null
    require(obj is ConfigurationSection) { "Click event has to be a configuration section or null!" }

    val actionString = obj.getString("action")
    val actionCanonicalName = actionString
        .replace(' ', '_')
        .toUpperCase()
    val action = ClickEvent.Action.valueOf(actionCanonicalName)

    val value = obj.getString("value")

    return ClickEvent(action, value)
}

/**
 * Deserializes a hover event.
 * @since 1.0
 */
fun deserializeHoverEvent(obj: Any?): HoverEvent? {
    if (obj == null) return null
    require(obj is ConfigurationSection) { "Hover event has to be a configuration section or null!" }

    val actionString = obj.getString("action")
    val actionCanonicalName = actionString
        .replace(' ', '_')
        .toUpperCase()
    val action = HoverEvent.Action.valueOf(actionCanonicalName)

    val value =
        deserializeTextComponent(obj.get("value"))

    return HoverEvent(action, arrayOf(value))
}

/**
 * Deserializes a book.
 * @since 1.0
 */
fun deserializeBook(obj: Any?): ItemStack? {
    val map = when (obj) {
        null -> return null
        is ConfigurationSection -> obj.getValues(true)
        is Map<*, *> -> obj.toMapOf<String, Any>()
        else -> throw IllegalArgumentException("Couldn't deserialize text component from ${obj::class.qualifiedName}!")
    }

    val stack = ItemStack(Material.WRITTEN_BOOK)

    val meta = stack.itemMeta as BookMeta

    meta.title = map["title"] as String?
    meta.author = map["author"] as String?
    meta.generation = deserializeBookGeneration(map["generation"])

    meta.spigot().pages = (map["pages"] as List<*>).map {
        arrayOf(deserializeTextComponent(it))
    }

    stack.itemMeta = meta

    return stack
}

/**
 * Deserializes book generation.
 * @since 1.0
 */
fun deserializeBookGeneration(obj: Any?): BookMeta.Generation? {
    if (obj == null) return null
    require(obj is String) { "Book generation has to be a string!" }

    val canonicalName = obj
        .replace(' ', '_')
        .toUpperCase()

    return BookMeta.Generation.valueOf(canonicalName)
}