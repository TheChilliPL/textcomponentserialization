package me.patrykanuszczyk.textcomponentserialization

import net.md_5.bungee.api.chat.ClickEvent
import net.md_5.bungee.api.chat.HoverEvent
import net.md_5.bungee.api.chat.TextComponent
import org.bukkit.ChatColor
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.BookMeta

@JvmSynthetic
internal fun formatString(string: String, placeholders: Map<String, String>? = null): String {
    var stringFormatted = string
    placeholders?.forEach { (key, value) ->
        stringFormatted = stringFormatted.replace("{$key}", value, true)
    }
    return ChatColor.translateAlternateColorCodes('&', stringFormatted)
}

fun TextComponent.format(placeholders: Map<String, String>? = null): TextComponent {
    text = formatString(text, placeholders)
    extra?.forEach { (it as TextComponent).format(placeholders) }
    insertion = formatString(insertion, placeholders)
    clickEvent = ClickEvent(clickEvent.action, formatString(clickEvent.value, placeholders))
    hoverEvent = HoverEvent(hoverEvent.action, hoverEvent.value.map {
        (it as TextComponent).format(placeholders)
    }.toTypedArray())
    return this
}

fun formatBook(book: ItemStack?, placeholders: Map<String, String>? = null): ItemStack? {
    if(book == null) return null

    val meta = book.itemMeta as BookMeta

    meta.apply {
        title = formatString(title, placeholders)
        author = formatString(author, placeholders)

        spigot().pages = spigot().pages.map { page ->
            page.map { component ->
                (component as TextComponent).format(placeholders)
            }.toTypedArray()
        }
    }

    book.itemMeta = meta

    return book
}