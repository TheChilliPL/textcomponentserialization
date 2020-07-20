package me.patrykanuszczyk.textcomponentserialization

import net.md_5.bungee.api.chat.TextComponent
import org.bukkit.configuration.ConfigurationSection

fun ConfigurationSection.getTextComponent(path: String): TextComponent? {
    return deserializeTextComponent(this.get(path))
}

fun ConfigurationSection.setTextComponent(path: String, value: TextComponent? = null) {
    this.set(path, value)
}

fun ConfigurationSection.getTextComponentList(path: String): List<TextComponent?> {
    return this.getList(path).map { deserializeTextComponent(it) }
}

fun ConfigurationSection.setTextComponentList(path: String, value: List<TextComponent?>) {
    this.set(path, value.map { it.serialize() })
}