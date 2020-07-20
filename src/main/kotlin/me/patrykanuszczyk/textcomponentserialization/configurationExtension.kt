package me.patrykanuszczyk.textcomponentserialization

import net.md_5.bungee.api.chat.TextComponent
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.inventory.ItemStack

fun ConfigurationSection.getTextComponent(path: String): TextComponent? {
    return deserializeTextComponent(this.get(path))
}

fun ConfigurationSection.setTextComponent(path: String, value: TextComponent?) {
    this.set(path, value.serialize())
}

fun ConfigurationSection.getTextComponentList(path: String): List<TextComponent?> {
    return this.getList(path).map { deserializeTextComponent(it) }
}

fun ConfigurationSection.setTextComponentList(path: String, value: List<TextComponent?>) {
    this.set(path, value.map { it.serialize() })
}

fun ConfigurationSection.getBook(path: String): ItemStack? {
    return deserializeBook(this.get(path))
}

fun ConfigurationSection.setBook(path: String, value: ItemStack?) {
    this.set(path, serializeBook(value))
}