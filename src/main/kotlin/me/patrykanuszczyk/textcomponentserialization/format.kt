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
    val newComponent = TextComponent(this)
    if (text != null) newComponent.text = formatString(text, placeholders)
    //extra?.forEach { newComponent.addExtra((it as TextComponent).format(placeholders)) }
    extra = extra.map { (it as TextComponent).format(placeholders) }
    if (insertion != null) newComponent.insertion = formatString(insertion, placeholders)
    if (clickEvent != null)
        newComponent.clickEvent = ClickEvent(clickEvent.action, formatString(clickEvent.value, placeholders))
    if (hoverEvent != null)
        newComponent.hoverEvent = HoverEvent(hoverEvent.action, hoverEvent.value.map {
            (it as TextComponent).format(placeholders)
        }.toTypedArray())
    return newComponent
}

fun formatBook(book: ItemStack?, placeholders: Map<String, String>? = null): ItemStack? {
    if (book == null) return null

    val oldMeta = book.itemMeta as BookMeta

    val newBook = ItemStack(book)
    
    val meta = newBook.itemMeta as BookMeta
    meta.title = formatString(oldMeta.title, placeholders)
    meta.author = formatString(oldMeta.author, placeholders)
    meta.generation = oldMeta.generation

    meta.spigot().addPage(*oldMeta.spigot().pages.map { page ->
        page.map {
            (it as TextComponent).format(placeholders)
        }.toTypedArray()
    }.toTypedArray())

    newBook.itemMeta = meta

    return newBook
}